/*
 * project:     COMPUCLICK
 * file:        Params.java
 * modified:    2023-12-02
 * name:        Ethan Mentzer
 * lab:         CSC 171-13
 */


// Import necessary classes
import java.awt.Dimension;
import java.awt.Font;


/**
 * The Params class contains all the adjustable visual parameters
 * used by various game components and classes, in addition to some
 * constants.
 */
public class Params {

    public static final Dimension GAME_SIZE = new Dimension(1280, 940);
    public static final int FRAME_MIN_WIDTH = 940;
    public static final int FRAME_MIN_HEIGHT = 500;
    public static final int ANSWER_FONT_SIZE = 35;
    public static final int EQUATION_FONT_SIZE = 65;
    public static final int COUNTDOWN_FONT_SIZE = 100;
    public static final int LABEL_FONT_SIZE = 16;
    public static final int HEADER_CUTOFF = 185;
    public static final Font ANSWER_FONT = new Font("Sans", Font.PLAIN, ANSWER_FONT_SIZE);
    public static final Font COUNTDOWN_FONT = new Font("Sans", Font.PLAIN, COUNTDOWN_FONT_SIZE);
    public static final Font EQUATION_FONT = new Font("Sans", Font.PLAIN, EQUATION_FONT_SIZE);
    public static final Font LABEL_FONT = new Font("Sans", Font.BOLD, LABEL_FONT_SIZE);

    // For converting between gameDifficulty (an int) and human-readable Strings
    public static final String[] DIFFICULTIES = {"Easy", "Normal", "Hard", "Impossible"};

}
