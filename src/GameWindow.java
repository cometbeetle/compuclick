/*
 * project:     COMPUCLICK
 * file:        GameWindow.java
 * modified:    2023-12-02
 * name:        Ethan Mentzer
 * lab:         CSC 171-13
 */


// Import necessary classes
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;


public class GameWindow extends JPanel {

    // Declare instance variables
    private AnswerChoice[] choices;
    private ModeButton[] modeButtons;
    private Timer countdownTimer;
    private ArrayList<Powerup> powerups = new ArrayList<>();
    private final WAVPlayer ding = new WAVPlayer("ding.wav", false);
    private final WAVPlayer beep = new WAVPlayer("beep.wav", false);
    private final WAVPlayer soundtrack = new WAVPlayer("soundtrack.wav", true);
    private final Random rng = new Random();

    // Game state variables
    private Equation eq;
    private String bestPlayer, highScoreDiff;
    private int score, highScore, numChoices, timeToAnswer, countdown;
    private int eqLevel, clickDifficulty, gameDifficulty;
    private long powerupAt, slowUntil, doublePointsUntil, easierUntil; // for powerups
    private boolean gameOver, titleScreen, slow, doublePoints, easier;

    // Constructor registers event handlers
    public GameWindow() {
        addMouseListener(new MouseHandler());
        addKeyListener(new KeyHandler());
        setFocusable(true);

        // Screen refreshes every 15 ms to facilitate animation
        Timer refreshTimer = new Timer(15, new RefreshTimerHandler());
        refreshTimer.start();

        // Set black background
        setBackground(Color.BLACK);

        // Tell paintComponent to draw the title screen
        titleScreen = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw title screen if necessary + return
        if (titleScreen) {
            drawTitleScreen(g);
            return;
        }

        // Draw the game interface
        drawGameUI(g);

        // Draw the answers (red if doublePoints is active)
        for (AnswerChoice choice : choices) {
            if (doublePoints)
                choice.draw(g, new Color(255, 110, 100));
            else
                choice.draw(g, Color.LIGHT_GRAY);
        }

        // Draw any powerups in the powerups ArrayList
        for (Powerup p : powerups) {
            p.draw(g);
        }

        // User instructions for new game
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.fillRect(getWidth() / 2 - 350, getHeight() / 2, 700, 150);
            g.setFont(Params.EQUATION_FONT);
            g.setColor(Color.RED);
            g.drawString("Press R to Restart", getWidth() / 2 - 280, getHeight() / 2 + 100);
        }
    }

    /**
     * drawGameUI draws the game interface (e.g., the score, timer, and equation).
     * Equation turns green when the "easier" powerup is active.
     * Countdown turns blue when the "slow" powerup is active.
     */
    private void drawGameUI(Graphics g) {
        // Draw toolbar labels
        g.setFont(Params.LABEL_FONT);
        g.setColor(Color.WHITE);
        if (slow)
            g.setColor(new Color(0, 140, 250));
        g.drawString("SECONDS REMAINING", 40, 135);
        g.setColor(Color.WHITE);
        g.drawString("SCORE", getWidth() - 125, 135);
        if (easier)
            g.setColor(new Color(128, 255, 128));
        g.drawString("SOLVE:", getWidth() / 2 - 30, 35);
        g.setColor(Color.WHITE);
        g.fillRect(0, Params.HEADER_CUTOFF, getWidth(), 5);

        // Draw equation centered on screen
        g.setFont(Params.EQUATION_FONT);
        if (easier)
            g.setColor(new Color(128, 255 ,128));
        g.drawString(eq.equation, getWidth() / 2 - halfStrWidth(eq.equation, g), 105);

        // Reset color to white
        g.setColor(Color.WHITE);

        // Set countdown font
        g.setFont(Params.COUNTDOWN_FONT);

        // Draw text + avoid drawing times less than 0
        String countdownString = String.format("%.1f", (double) (countdown) / 100.0);
        if (slow)
            g.setColor(new Color(0, 140, 250));
        if (countdown >= 0)
            g.drawString(countdownString, 125 - halfStrWidth(countdownString, g), 100);
        else
            g.drawString("0.0", 125 - halfStrWidth("0.0", g), 100);

        // Reset color to white
        g.setColor(Color.WHITE);

        // Draw score
        g.drawString(String.valueOf(score), getWidth() - 100 - halfStrWidth(String.valueOf(score), g), 100);

        // Draw high score info
        g.setFont(Params.LABEL_FONT);
        g.setColor(new Color(0, 100, 200));
        String hsString = "HIGH SCORE: " + highScore + "  |  Player: " + bestPlayer + "  |  Difficulty: " + highScoreDiff;
        g.drawString(hsString, getWidth() / 2 - halfStrWidth(hsString, g), 165);
    }

    /**
     * drawTitleScreen creates the game difficulty selector and is meant to be run on game startup.
     */
    private void drawTitleScreen(Graphics g) {
        modeButtons = new ModeButton[4];

        // Fill modeButtons array with 4 buttons evenly spaced in the middle of the screen
        for (int i = 0; i < modeButtons.length; i++) {
            modeButtons[i] = new ModeButton(getWidth() / 2 - modeButtons.length * 110 + 220 * i, getHeight() / 2, i);
        }

        // Draw buttons
        for (ModeButton b : modeButtons) {
            b.draw(g);
        }

        // Draw game title
        g.setColor(Color.WHITE);
        g.fillRect(getWidth() / 2 - 350, getHeight() / 2 - 200, 700, 150);
        g.setFont(new Font("Monospaced", Font.BOLD, Params.EQUATION_FONT_SIZE));
        g.setColor(Color.BLUE);
        g.drawString("COMPUCLICK", getWidth() / 2 -
                halfStrWidth("COMPUCLICK", g), getHeight() / 2 - 120);
        g.setColor(Color.BLACK);
        g.setFont(Params.LABEL_FONT);

        g.drawString("Please select a difficulty:", getWidth() / 2 -
                halfStrWidth("Please select a difficulty", g), getHeight() / 2 - 70);
    }

    /**
     * resetGame starts a new game, resetting the game state to initial conditions.
     */
    private void resetGame() {
        // Generate first equation (level 0)
        eq = new Equation(0);

        // Initial conditions (change here if needed)
        slow = false;
        doublePoints = false;
        easier = false;
        score = 0;
        numChoices = 2;
        eqLevel = 0;
        clickDifficulty = 1;
        timeToAnswer = 2000; // 20 seconds initially

        // Get current high score information from the file
        Object[] highScoreInfo = HighScore.getHighScore(gameDifficulty);

        // Only attempt to set instance variables if highScoreInfo contains non-null data
        if (highScoreInfo[0] != null) {
            highScore = (int) highScoreInfo[1];
            bestPlayer = (String) highScoreInfo[0];
            highScoreDiff = (String) highScoreInfo[2];
        } else {
            highScore = 0;
            bestPlayer = "Current Player";
            highScoreDiff = Params.DIFFICULTIES[gameDifficulty];
        }

        // Initialize choices based on mode
        resetChoices();

        // Remove any powerups + schedule new powerup
        powerups = new ArrayList<>();
        powerupAt = rng.nextLong(System.currentTimeMillis() + 15000, System.currentTimeMillis() + 30000);

        countdown = timeToAnswer;

        // Countdown timer events happen every 100 ms
        countdownTimer = new Timer(100, new CountdownTimerHandler());
        countdownTimer.stop();
        countdownTimer.start();

        // Play looping soundtrack from beginning
        soundtrack.playSound();

        // This is at the end to ensure anything dependent on it waits until game reset is complete
        gameOver = false;
    }

    /**
     * resetChoices re-instantiates the Array of choices, and adds new randomized values.
     */
    private void resetChoices() {
        // Create list of choices
        choices = new AnswerChoice[numChoices];

        /* Now, we add the true answer to the Array first to improve mouse detection.
         * As a result, we can simply check to see if the mouse was pressed over the first
         * AnswerChoice in the Array. This ultimately means that the player gets the right answer
         * even if they click on 2 answers at once (if 2 answers are on top of each other).
         */
        int randomX = rng.nextInt(Params.ANSWER_FONT_SIZE + 50, getWidth() - 50);
        int randomY = rng.nextInt(Params.HEADER_CUTOFF + Params.ANSWER_FONT_SIZE, getHeight() - 50);

        choices[0] = new AnswerChoice(eq.answer, randomX, randomY, true);

        // Add incorrect answer choices to list + set coordinates randomly
        for (int i = 1; i < numChoices; i++) {
            randomX = rng.nextInt(Params.ANSWER_FONT_SIZE + 50, getWidth() - 50);
            randomY = rng.nextInt(Params.HEADER_CUTOFF + Params.ANSWER_FONT_SIZE, getHeight() - 50);

            // Generate wrong answer
            int randomWrongAnswer = eq.answer + rng.nextInt(-100, 100);

            // Special handling if it's a hard equation (bypass answer ints and use Strings for everything)
            if (eq.hard) {
                choices[0].answerAsString = eq.answerAsString;
                choices[0].calculateHitbox();

                choices[i] = new AnswerChoice(randomWrongAnswer, randomX, randomY, false);

                // Random shift
                int randShift = rng.nextInt(-15, 16);

                // Avoid a wrong answer that is actually correct
                if (randShift == 0)
                    randShift += 1;

                choices[i].answerAsString = String.format("%.1f", Double.parseDouble(eq.answerAsString) + randShift / 10.0);
                choices[i].calculateHitbox();
                continue;
            }

            // Make sure not to add the correct answer as one of the random answers
            if (randomWrongAnswer == eq.answer)
                choices[i] = new AnswerChoice(randomWrongAnswer + 1, randomX, randomY, false);
            else
                choices[i] = new AnswerChoice(randomWrongAnswer, randomX, randomY, false);
        }
    }

    /**
     * nextRound advances the game to the next round by increasing the number
     * of possible answers (50% chance), and by decreasing the amount of time
     * (random amount between 10 and 200 ms).
     * <br><br>
     * If in easy mode, time will not decrease, and will always be reset to 20 seconds.
     * <br><br>
     * If in normal mode, time will decrease until equation level goes up (every 25 points).
     * <br><br>
     * If in hard mode or impossible mode, time will always decrease.
     */
    private void nextRound() {
        int decreaseAmount;
        int pointsAdded = 1; // to deal with double points powerup

        if (gameDifficulty == 0)
            decreaseAmount = 0;
        else
            decreaseAmount = rng.nextInt(10, 200);

        // Play ding
        ding.playSound();

        // Increment game state
        timeToAnswer -= decreaseAmount;
        if (doublePoints) {
            score += 2;
            pointsAdded = 2;
        } else
            score++;

        // Add another answer choice at random
        if (rng.nextBoolean())
            numChoices++;


        /* Increase the equation difficulty by 1 every 25 points + reset other state variables.
         * This also accounts for if the double points powerup is active.
         */
        boolean singlePointsBypass = score % 25 == 0;
        boolean doublePointsBypass = score % 25 == 1 && pointsAdded == 2;

        if ((singlePointsBypass || doublePointsBypass) && eqLevel < 2) {
            eqLevel++;
            numChoices = 2;
            clickDifficulty = 1;

            // Only reset timeToAnswer if difficulty is normal or easy
            if (gameDifficulty <= 1)
                timeToAnswer = 2000;
        }

        // Increase click difficulty by 1 every 4 points
        if (score % 4 == 0)
            clickDifficulty++;

        // High score monitoring
        if (score > highScore) {
            highScore = score;
            bestPlayer = "Current Player";
        }

        // Generate new equation
        eq = new Equation(eqLevel);

        resetChoices();

        // Reset countdown timer
        countdown = timeToAnswer;
    }

    /**
     * gameOver stops the countdownTimer. It also plays a sound to alert the player
     * the game has ended, and saves the score (if it's a new high score) to a file.
     */
    private void gameOver() {
        gameOver = true;
        countdownTimer.stop();

        // Reset background color + powerup activations
        setBackground(Color.BLACK);
        doublePoints = false;
        slow = false;
        easier = false;

        // Sound effects
        soundtrack.stopSound();
        beep.playSound();

        // Stop answer choice movement
        for (AnswerChoice choice : choices) {
            if (choice == null) break;
            choice.dx = 0;
            choice.dy = 0;
        }

        // Prompt user to enter name if new high score
        if (bestPlayer.equals("Current Player")) {
            HighScore.newHighScore(highScore, Params.DIFFICULTIES[gameDifficulty]);
        }
    }

    /**
     * halfStrWidth consolidates the code used to find half the length of
     * a string (this is helpful for centering strings on the screen).
     */
    private int halfStrWidth(String str, Graphics g) {
        return g.getFontMetrics().stringWidth(str) / 2;
    }


    /* -----------------------------------------------------------------------
     * Below are event and action handler inner classes for code cleanliness.
     * -----------------------------------------------------------------------
     */

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);

            // See if mouse was pressed within a title screen mode selection
            if (titleScreen && modeButtons != null) {
                for (ModeButton b : modeButtons) {
                    // Set game difficulty based on clicked button
                    if (b.contains(e.getPoint())) {
                        gameDifficulty = b.mode;
                        titleScreen = false;
                        resetGame();

                        // Return to prevent accidentally clicking on answer at the same time
                        return;
                    }
                }
            }

            // Handle powerup clicking before answer choices
            Powerup toRemove = null;
            for (Powerup p : powerups) {
                if (p == null) break;

                if (!gameOver && p.contains(e.getPoint())) {
                    toRemove = p;

                    switch (p.type) {
                        // Slow time
                        case 0:
                            countdownTimer.setDelay(200);
                            slowUntil = System.currentTimeMillis() + 10000;
                            setBackground(new Color(0, 0, 20));
                            slow = true;
                            break;
                        // Double points for new clicks
                        case 1:
                            doublePointsUntil = System.currentTimeMillis() + 10000;
                            setBackground(new Color(20, 0 ,0));
                            doublePoints = true;
                            break;
                        // Make equation easier by 1 level
                        case 2:
                            eqLevel--;

                            // Keep time the same even though round technically advances
                            int oldTime = countdown;
                            nextRound();
                            countdown = oldTime;

                            // Avoid counteracting the wrong number of points
                            if (doublePoints)
                                score -= 2;
                            else
                                score--;

                            easierUntil = System.currentTimeMillis() + 10000;
                            setBackground(new Color(0, 20, 0));
                            easier = true;
                            break;
                    }
                }
            }

            // Remove clicked powerup here to avoid ConcurrentModificationException
            if (toRemove != null) {
                powerups.remove(toRemove);
                return; // return to prevent also clicking on answer at same time
            }

            // See if mouse was pressed within correct answer choice + advance game if so
            if (choices != null) {
                for (AnswerChoice choice : choices) {
                    if (choice == null) break;

                    // Advance round if user clicks correct choice
                    if (!gameOver && choice.contains(e.getPoint()) && choice.correct) {
                        nextRound();
                        break;
                    }

                    // End game if difficulty is impossible and user clicks anywhere that's not the right answer
                    else if (!gameOver && gameDifficulty == 3 && !choice.contains(e.getPoint()))
                        gameOver();

                        // End game if clicked on wrong answer
                    else if (!gameOver && choice.contains(e.getPoint()) && !choice.correct)
                        gameOver();
                }
            }
        }
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            if (e.getKeyCode() == KeyEvent.VK_R && gameOver)
                titleScreen = true;

        }
    }

    private class CountdownTimerHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // Count down & end game if timer reaches 0
            if (countdown > 0)
                countdown -= 10;
            else
                gameOver();

        }
    }

    private class RefreshTimerHandler implements ActionListener {
        double angle = 0.0; // Used to randomize motion based on sine wave

        @Override
        public void actionPerformed(ActionEvent e) {
            if (choices == null) return; // error catching
            for (AnswerChoice choice : choices) {
                if (choice == null) break;

                // Check if choice is out of bounds + fix if so
                choice.checkOutOfBounds(GameWindow.this);

                // Random motion based on clickDifficulty
                if (countdown % rng.nextInt(80 / clickDifficulty, 100) == 0 && !gameOver) {
                    choice.dx = (int) (rng.nextGaussian(0, clickDifficulty) * Math.cos(angle));
                    choice.dy = (int) (rng.nextGaussian(0, clickDifficulty) * Math.sin(angle));

                    // Make velocities slow if slow powerup is selected
                    if (slow) {
                        choice.dx = (int) rng.nextGaussian(0, 1);
                        choice.dy = (int) rng.nextGaussian(0, 1);
                    }
                }

                choice.moveAnswer();
            }

            // Add a powerup every ~15-30 seconds
            if (!gameOver && System.currentTimeMillis() >= powerupAt) {
                long now = System.currentTimeMillis();
                powerupAt = rng.nextLong(now + 15000, now + 30000);

                // Randomize powerup location and type
                int randomType;
                int randomX = rng.nextInt(Params.ANSWER_FONT_SIZE + 50, getWidth() - 50);
                int randomY = rng.nextInt(Params.HEADER_CUTOFF + Params.ANSWER_FONT_SIZE, getHeight() - 50);

                // Only make equation easier if it's not already the easiest
                if (eqLevel > 0)
                    randomType = rng.nextInt(0, 3);
                else
                    randomType = rng.nextInt(0, 2);

                powerups.add(new Powerup(randomX, randomY, randomType));
            }

            // Make sure powerups don't go out of bounds if window is resized
            for (Powerup p : powerups) {
                if (p == null) break;
                p.checkOutOfBounds(GameWindow.this);
            }

            // Remove powerups when they expire (next 3 if statements)
            if (slow && System.currentTimeMillis() >= slowUntil) {
                setBackground(Color.BLACK);
                slow = false;
                countdownTimer.setDelay(100);
            }

            if (doublePoints && System.currentTimeMillis() >= doublePointsUntil) {
                setBackground(Color.BLACK);
                doublePoints = false;
            }

            if (easier && System.currentTimeMillis() >= easierUntil) {
                setBackground(Color.BLACK);
                easier = false;
                eqLevel++;
            }

            // Increment angle
            angle += 0.1;
            repaint();
        }
    }

}
