/*
 * project:     COMPUCLICK
 * file:        AnswerChoice.java
 * modified:    2023-12-02
 * name:        Ethan Mentzer
 */


// Import necessary classes
import javax.swing.JPanel;
import java.awt.*;
import java.util.Random;


/**
 * The AnswerChoice class stores information about each answer choice
 * that floats around the screen.
 */
public class AnswerChoice {

    // Declare instance variables
    public int x, y, dx, dy;
    public double answer;
    public String answerAsString;
    public boolean correct;
    public Rectangle hitBox;

    // Constructor
    public AnswerChoice(double answer, int x, int y, boolean correct) {
        this.answer = answer;
        this.x = x;
        this.y = y;
        this.correct = correct;

        // Random initial dx and dy between -1 and 1
        Random rng = new Random();
        dx = rng.nextInt(-1, 1);
        dy = rng.nextInt(-1, 1);

        // Convert double to array of chars
        char[] charArray = String.valueOf(answer).toCharArray();

        // Remove decimal information if ends in .0
        if (charArray[charArray.length - 1] == '0' && charArray[charArray.length - 2] == '.') {
            char[] fixedArray = new char[charArray.length - 2];
            System.arraycopy(charArray, 0, fixedArray, 0, fixedArray.length);
            answerAsString = new String(fixedArray);
        } else
            answerAsString = new String(charArray);

        calculateHitbox();
    }

    /**
     * calculateHitbox is useful when answerAsString changes (such as when the equation
     * happens to be a "hard" equation).
     */
    public void calculateHitbox() {
        // Use a JPanel to find how big the string will be on screen
        JPanel fontPanel = new JPanel();
        int fontBound = fontPanel.getFontMetrics(Params.ANSWER_FONT).stringWidth(answerAsString);

        // Create rectangle based on font characteristics
        hitBox = new Rectangle(x - 5, y - Params.ANSWER_FONT_SIZE, fontBound + 15,
                Params.ANSWER_FONT_SIZE + 10);
    }

    /**
     * contains returns true if a supplied point is within the choice area.
     */
    public boolean contains(Point p) {
        return hitBox.contains(p);
    }


    /**
     * draw moves the drawing code into the object, cleaning up paintComponent.
     * This method takes a Color as a parameter for use with powerups.
     */
    public void draw(Graphics g, Color c) {
        g.setFont(Params.ANSWER_FONT);
        g.setColor(c);
        g.fillRoundRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height, 10, 10);
        g.setColor(Color.BLACK);
        g.drawString(answerAsString, x, y);
    }

    /**
     * moveAnswer shifts the position and hitBox of the object by dx and dy.
     */
    public void moveAnswer() {
        x += dx;
        y += dy;

        calculateHitbox();
    }

    /**
     * checkOutOfBounds takes a GameWindow as a parameter in order to gauge the size
     * of the GameWindow. It then makes sure the current object is not out of bounds,
     * adjusting it if so.
     */
    public void checkOutOfBounds(GameWindow panel) {
        /* The next 4 if statements detect when objects move beyond the edges of the screen.
         * It is a bit confusing, as the AnswerChoice objects' x and y coordinates are
         * somewhere within the hitBoxes, so we need to account for that, while still
         * updating x and y (we cannot just update the hitBox x and y because of how
         * AnswerChoice is currently implemented).
         */
        if (hitBox.x <= 0) {
            dx *= -1;
            x = x - hitBox.x;
        }

        if (hitBox.x + hitBox.width >= panel.getWidth()) {
            dx *= -1;
            x = panel.getWidth() - (hitBox.x + hitBox.width - x);
        }

        if (hitBox.y <= Params.HEADER_CUTOFF) {
            dy *= -1;
            y = Params.HEADER_CUTOFF + hitBox.height - (hitBox.y + hitBox.height - y);
        }

        if (hitBox.y + hitBox.height >= panel.getHeight()) {
            dy *= -1;
            y = panel.getHeight() - (hitBox.y + hitBox.height - y);
        }
    }

}
