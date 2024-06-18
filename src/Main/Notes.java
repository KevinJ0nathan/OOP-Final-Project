package Main;

import com.sun.tools.javac.Main;

import java.awt.*;
/**
 * This class represents a note in a rhythm game.
 * Each note has properties such as position, size, speed, color, and active status.
 */
public class Notes implements Beat{
    // Position, size, and speed of the note
    public int x, y, width, height, speed;
    // Color of the note
    public Color color;
    // Active status of the note
    public boolean active;

    /**
     * Constructs a note with the specified properties.
     *
     * @param x      The x-coordinate of the note.
     * @param y      The y-coordinate of the note.
     * @param width  The width of the note.
     * @param height The height of the note.
     * @param speed  The speed at which the note moves.
     * @param color  The color of the note.
     */
    public Notes(int x, int y, int width, int height, int speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
        this.active = true;
    }

    /**
     * Update the position of the note to go down the screen based on the speed it was set.
     * If the note reaches the bottom of the screen, it becomes inactive and triggers a miss event.
     * If the combo is not zero, it resets the combo and plays the combo break sound.
     *
     * @param combo         The current amount of combos the player has.
     * @param comboBreak    comboBreak is the sound played when combo breaks.
     * @return combo        The updated combo count.
     */
    @Override
    public int update(int combo, Music comboBreak) {
        y += speed;
        if (y > 730) {
            active = false;
            GamePanel.scoreTypeDisplay = "miss";
            GamePanel.missCounter += 1;
            if(combo != 0) {
                combo = 0;
                comboBreak.play();
            }
        }
        return combo;
    }

    /**
     * Draws the note on the screen.
     *
     * @param g2d The graphics context.
     */
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setPaint(color);
        g2d.fillRect(x, y, width, height);
    }
    // Getter and setter methods for encapsulation
    @Override
    public void setX(int x) {
        this.x = x;
    }
    @Override
    public void setY(int y) {
        this.y = y;
    }
    @Override
    public int getWidth() {
        return width;
    }
    @Override
    public void setWidth(int width) {
        this.width = width;
    }
    @Override
    public int getHeight() {
        return height;
    }
    @Override
    public void setHeight(int height) {
        this.height = height;
    }
    @Override
    public int getSpeed() {
        return speed;
    }
    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    @Override
    public Color getColor() {
        return color;
    }
    @Override
    public void setColor(Color color) {
        this.color = color;
    }
    @Override
    public boolean isActive() {
        return active;
    }
    @Override
    public void setActive(boolean bool){
        this.active = bool;
    }
    @Override
    public int getX() {
        return this.x;
    }
    @Override
    public int getY() {
        return this.y;
    }
}
