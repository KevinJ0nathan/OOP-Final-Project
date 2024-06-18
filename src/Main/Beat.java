package Main;

import java.awt.*;

public interface Beat {
    int update(int combo, Music comboBreak);
    void draw(Graphics2D g2d);
    void setX(int x);
    void setY(int y);
    int getWidth();
    void setWidth(int width);
    int getHeight();
    void setHeight(int height);
    int getSpeed();
    void setSpeed(int speed);
    Color getColor();
    void setColor(Color color);
    boolean isActive();
    void setActive(boolean bool);
    int getX();
    int getY();
}