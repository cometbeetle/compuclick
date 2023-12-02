/*
 * project:     COMPUCLICK
 * file:        Powerup.java
 * modified:    2023-12-02
 * name:        Ethan Mentzer
 * lab:         CSC 171-13
 */


// Import necessary classes
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;


/**
 * The Powerup class stores information about powerups that appear on screen.
 * This includes position and powerup type (0 represents slow down time,
 * 1 represents double points, 2 represents decreased equation difficulty).
 * All powerups last 10 seconds.
 */
public class Powerup {

    public int x, y, type;
    private final int r1, r2;
    private final String powerUpText;
    private final Color color;

    // Constructor
    public Powerup(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;

        // Ellipse sizing
        r1 = 140;
        r2 = 64;

        switch (type) {
            case 0:
                powerUpText = "Slow";
                color = new Color(0, 0, 139);
                break;
            case 1:
                powerUpText = "Points x2";
                color = new Color(255, 49, 49);
                break;
            case 2:
                powerUpText = "Easier";
                color = new Color(50, 205, 50);
                break;
            default:
                powerUpText = "NULL";
                color = Color.WHITE;
        }
    }

    /**
     * contains returns true if a supplied point is within the ellipse.
     */
    public boolean contains(Point p) {
        double xx = p.x - this.x;
        double yy = p.y - this.y;
        double r1 = this.r1 / 2.0;
        double r2 = this.r2 / 2.0;

        return ((xx*xx)/(r1*r1) + (yy*yy)/(r2*r2) <= 1.0);
    }

    /**
     * checkOutOfBounds takes a GameWindow as a parameter in order to gauge the size
     * of the GameWindow. It then makes sure the current object is not out of bounds,
     * adjusting it if so.
     */
    public void checkOutOfBounds(GameWindow panel) {
        //The next 4 if statements detect when objects move beyond the edges of the screen.
        if (x <= 0)
            x = 1;

        if (x + r1/2 >= panel.getWidth())
            x = panel.getWidth() - r1/2 - 1;

        if (y <= Params.HEADER_CUTOFF) {
            y = Params.HEADER_CUTOFF + r2/2 + 1;
        }

        if (y + r2/2 >= panel.getHeight()) {
            y = panel.getHeight() - r2/2 - 1;
        }
    }

    /**
     * draw moves the drawing code into the object, cleaning up paintComponent.
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x-r1/2, y-r2/2, r1, r2);
        g.setFont(Params.LABEL_FONT);
        g.setColor(Color.WHITE);
        g.drawString(powerUpText, x - g.getFontMetrics().stringWidth(powerUpText) / 2, y+5);
    }

}
