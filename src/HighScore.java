/*
 * project:     COMPUCLICK
 * file:        HighScore.java
 * modified:    2023-12-02
 * name:        Ethan Mentzer
 * lab:         CSC 171-13
 */


// Import necessary classes
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * The HighScore class contains the methods necessary to prompt a user to
 * enter their name, and to save their score (meant to be a high score) to a file.
 */
public class HighScore implements ActionListener {

    // Declare frame
    private static JFrame frame;

    // Variables for writing to the file
    private static JTextField field;
    private static String difficulty;
    private static int score;

    /**
     * newHighScore creates the GUI.
     */
    public static void newHighScore(int score, String difficulty) {
        // Set file writer variables
        HighScore.difficulty = difficulty;
        HighScore.score = score;

        // Set fonts
        Font labelFont = new Font("Sans", Font.PLAIN, 18);
        Font headerFont = new Font("Sans", Font.BOLD, 30);

        // Create frame
        frame = new JFrame("New High Score!");

        // Create main sub panel
        JPanel wholePanel = new JPanel();
        wholePanel.setLayout(new BoxLayout(wholePanel, BoxLayout.PAGE_AXIS));

        // Create sub-sub panel 1
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Create sub-sub panel 2
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.LINE_AXIS));

        // Add high score text
        JLabel highScore = new JLabel("New High Score!");
        highScore.setForeground(Color.BLUE);
        highScore.setFont(headerFont);
        upperPanel.add(highScore);

        // Add instructions
        JLabel label = new JLabel(" Enter your name: ");
        label.setFont(labelFont);
        lowerPanel.add(label);

        // Add user input field
        field = new JTextField();
        field.addActionListener(new HighScore());
        field.setColumns(25);
        field.setFont(labelFont);
        lowerPanel.add(field);

        JButton submit = new JButton("Submit");
        submit.addActionListener(new HighScore());
        submit.setFont(labelFont);
        lowerPanel.add(submit);

        // Add sub-sub panels to the sub panel
        wholePanel.add(upperPanel);
        wholePanel.add(new JLabel("\n")); // spacing
        wholePanel.add(lowerPanel);
        wholePanel.add(new JLabel("\n")); // spacing

        // Add the sub panel to the frame + set sizing
        frame.add(wholePanel, BorderLayout.NORTH);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    // Method implementation
    public void actionPerformed(ActionEvent e) {
        e.setSource(field); // Always perform the action on the text input field
        JTextField source = (JTextField) e.getSource(); // type cast to JTextField

        String fileName = "highScores.csv";
        String name = source.getText();

        // Anonymous if no name provided
        if (name.isEmpty())
            name = "Anonymous";

        // Try with resources to append to file
        try (FileWriter fw = new FileWriter(fileName, true)) {
            PrintWriter printer = new PrintWriter(fw);
            printer.println(String.format("%s,%d,%s", name, score, difficulty));
            printer.close();
        } catch (Exception err) {
            System.err.println("ERROR: " + err);
        }

        frame.dispose();
        frame.removeAll();
    }

    /**
     * getHighScore returns an array of objects with a player name, their high score,
     * and the difficulty they played at.
     */
    public static Object[] getHighScore(int diff) {

        // Convert difficulty int into String value to search for
        difficulty = switch(diff) {
            case 0 -> "Easy";
            case 1 -> "Normal";
            case 2 -> "Hard";
            case 3 -> "Impossible";
            default -> null;
        };

        String fileName = "highScores.csv";
        Scanner s, lineReader;

        // Try with resources to read from file
        try (FileReader fr = new FileReader(fileName)) {
            s = new Scanner(fr);
            String lastLine = null;

            // Get last line by difficulty
            while (s.hasNextLine()) {
                String line = s.nextLine();

                lineReader = new Scanner(line);
                lineReader.useDelimiter(",");

                // Difficulty is last, so we read until the last token
                while (lineReader.hasNext()) {
                    if (lineReader.next().equals(difficulty)) {
                        lastLine = line;
                    }
                }
            }

            // Return blank array if no score was found for specified difficulty
            if (lastLine == null)
                return new Object[3];

            // Re-instantiate the Scanner to read from the last line
            s = new Scanner(lastLine);
            s.useDelimiter(",");

            // Extract values
            String name = s.next();
            int highScore = s.nextInt();

            // Return as an array
            return new Object[]{name, highScore, difficulty};

        } catch (InputMismatchException err) {
            System.out.println("WARNING: most recent score corrupted (returning blank array...)");
            return new Object[3]; // blank on exception
        } catch (Exception err) {
            System.out.println("WARNING: " + err);
            return new Object[3]; // blank on exception
        }
    }

}