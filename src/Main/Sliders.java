package Main;

import java.awt.*;

public class Sliders extends Notes {
    private Rectangle topNode;
    private Rectangle bottomNode;

    public Sliders(int x, int y, int width, int height, int speed, Color color) {
        super(x, y, width, height, speed, color);
        this.topNode = new Rectangle(x, y, width, 30);
        this.bottomNode = new Rectangle(x, y + height - 10, width, 30);
    }

    @Override
    public int update(int combo, Music comboBreak) {
        y += speed;
        if (topNode.y > 720) {
            active = false;
            if(combo != 0) {
                combo = 0;
                comboBreak.play();
            }
        }
        topNode.setLocation(x, y);
        bottomNode.setLocation(x, y + height - 10);
        return combo;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(x+10, y, width-20, height);
        g2d.setPaint(Color.PINK);
        g2d.fill(topNode);
        g2d.fill(bottomNode);
    }

    public Rectangle getTopNode() {
        return topNode;
    }

    public Rectangle getBottomNode() {
        return bottomNode;
    }
}
