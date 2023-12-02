/*
 * project:     COMPUCLICK
 * file:        Main.java
 * modified:    2023-12-02
 * name:        Ethan Mentzer
 * lab:         CSC 171-13
 */


// Import necessary classes
import javax.swing.JFrame;
import java.awt.Dimension;


/**
 * The Main class creates the JFrame in which our game runs.
 */

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("COMPUCLICK");

        Dimension gameSize = Params.GAME_SIZE;
        Dimension minSize = new Dimension(Params.FRAME_MIN_WIDTH, Params.FRAME_MIN_HEIGHT);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(minSize);
        frame.setSize(gameSize);
        frame.add(new GameWindow());
        frame.setVisible(true);
    }

}

