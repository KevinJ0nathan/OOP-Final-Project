package Main;

import java.awt.*;
/**
 * This class represents a note in a rhythm game
 * Each note has properties such as position, size, speed, color, and active status.
 */
public class Notes {
    public int x, y, width, height, speed;
    public Color color;
    public boolean active;
    public Notes(int x, int y, int width, int height, int speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
        this.active = true;
    }

    public int update(int combo, Music comboBreak) {
        y += speed;
        if (y > 720) {
            active = false;
            if(combo != 0) {
                combo = 0;
                comboBreak.play();
            }
        }
        return combo;
    }

    // draw notes on screen
    public void draw(Graphics2D g2d) {
        g2d.setPaint(color);
        g2d.fillRect(x, y, width, height);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean bool){
        this.active = bool;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
