package Main;

import java.awt.*;

/**
 * This class represents a slider note in a rhythm game.
 * A slider note extends the basic note with additional top and bottom nodes.
 */
public class Sliders extends Notes {
    // Rectangles representing the top and bottom nodes of the slider
    private Rectangle topNode;
    private Rectangle bottomNode;

    /**
     * Constructs a slider note with the specified properties.
     *
     * @param x      The x-coordinate of the slider.
     * @param y      The y-coordinate of the slider.
     * @param width  The width of the slider.
     * @param height The height of the slider.
     * @param speed  The speed at which the slider moves.
     * @param color  The color of the slider.
     */
    public Sliders(int x, int y, int width, int height, int speed, Color color) {
        super(x, y, width, height, speed, color);
        this.topNode = new Rectangle(x, y, width, 30);
        this.bottomNode = new Rectangle(x, y + height - 10, width, 30);
    }

    /**
     * Updates the position of the slider to move down the screen based on the speed.
     * If the top node reaches the bottom of the screen, the slider becomes inactive and triggers a miss event.
     * The positions of the top and bottom nodes are updated to match the new position of the slider.
     *
     * @param combo      The current combo count of the player.
     * @param comboBreak The sound played when the combo breaks.
     * @return The updated combo count.
     */
    @Override
    public int update(int combo, Music comboBreak) {
        y += speed;
        if (topNode.y > 730) {
            active = false;
            GamePanel.scoreTypeDisplay = "miss";
            GamePanel.missCounter += 1;
            if(combo != 0) {
                combo = 0;
                comboBreak.play();
            }
        }
        topNode.setLocation(x, y);
        bottomNode.setLocation(x, y + height - 10);
        return combo;
    }

    /**
     * Draws the slider on the screen.
     * The main body of the slider is drawn in white, and the top and bottom nodes are drawn in pink.
     *
     * @param g2d The graphics context.
     */
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(x + 10, y, width - 20, height);
        g2d.setPaint(Color.PINK);
        g2d.fill(topNode);
        g2d.fill(bottomNode);
    }

    /**
     * Returns the top node of the slider.
     *
     * @return The top node rectangle.
     */
    public Rectangle getTopNode() {
        return topNode;
    }

    /**
     * Returns the bottom node of the slider.
     *
     * @return The bottom node rectangle.
     */
    public Rectangle getBottomNode() {
        return bottomNode;
    }
}
