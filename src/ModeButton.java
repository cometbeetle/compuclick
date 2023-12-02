/*
 * project:     COMPUCLICK
 * file:        ModeButton.java
 * modified:    2023-12-02
 * name:        Ethan Mentzer
 * lab:         CSC 171-13
 */


// Import necessary classes
import java.awt.*;


/**
 * The ModeButton class stores information about the game difficulty selector
 * buttons on the title screen. This makes it easy to check whether the user
 * clicked on a button or not. Mode is used to store which game mode the button
 * selects (a number from 0 to 3).
 */
public class ModeButton {

    public final int x, y, mode;
    public final String modeString;
    public final Rectangle hitBox;
    public final Color color;

    // Constructor
    public ModeButton(int x, int y, int mode) {
        this.x = x;
        this.y = y;
        this.mode = mode;

        hitBox = new Rectangle(x, y, 200, 50);

        // Set modeString using enhanced switch statement
        modeString = Params.DIFFICULTIES[mode];

        // Set modeString using enhanced switch statement
        color = switch (mode) {
            case 0 -> new Color(50, 205, 50);
            case 1 -> new Color(0, 125, 255);
            case 2 -> Color.ORANGE;
            case 3 -> Color.RED;
            default -> Color.WHITE;
        };
    }

    /**
     * contains returns true if a supplied point is within the button area.
     */
    public boolean contains(Point p) {
        return hitBox.contains(p);
    }

    /**
     * draw moves the drawing code into the object, cleaning up paintComponent.
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRoundRect(x, y, hitBox.width, hitBox.height, 10, 10);
        g.setColor(Color.BLACK);
        g.setFont(Params.ANSWER_FONT);
        g.drawString(modeString, x + hitBox.width/2 - g.getFontMetrics().stringWidth(modeString) / 2, y+35);
    }

}
