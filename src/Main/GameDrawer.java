package Main;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class GameDrawer extends JPanel {
    // Images for different types of scores
    private Image perfectScore;
    private Image okayScore;
    private Image badScore;
    private Image miss;

    // Constructor to initialize score images
    GameDrawer(){
        perfectScore = Toolkit.getDefaultToolkit().getImage("assets/hit30.png");
        okayScore = Toolkit.getDefaultToolkit().getImage("assets/hit10.png");
        badScore = Toolkit.getDefaultToolkit().getImage("assets/hit5.png");
        miss = Toolkit.getDefaultToolkit().getImage("assets/miss.png");
    }

    // Draw the vertical lanes on the screen
    public void drawLanes(Graphics2D g2d) {
        g2d.drawLine(100, 0, 100, 800);
        g2d.drawLine(200, 0, 200, 800);
        g2d.drawLine(300, 0, 300, 800);
        g2d.drawLine(400, 0, 400, 800);
        g2d.drawLine(500, 0, 500, 800);
    }

    // Draw the rectangles representing the keys
    public void drawRectangles(Graphics2D g2d, Color dRectangleCurrentColor, Color fRectangleCurrentColor, Color jRectangleCurrentColor, Color kRectangleCurrentColor) {
        g2d.setPaint(dRectangleCurrentColor);
        g2d.fillRect(101, 700, 98, 100);
        g2d.setPaint(fRectangleCurrentColor);
        g2d.fillRect(201, 700, 98, 100);
        g2d.setPaint(jRectangleCurrentColor);
        g2d.fillRect(301, 700, 98, 100);
        g2d.setPaint(kRectangleCurrentColor);
        g2d.fillRect(401, 700, 98, 100);
    }

    // Draw the key binds (D, F, J, K) above their respective rectangles
    public void drawKeyBinds(Graphics2D g2d) {
        g2d.setPaint(Color.black);
        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        g2d.drawString("D", 135, 770);
        g2d.drawString("F", 235, 770);
        g2d.drawString("J", 335, 770);
        g2d.drawString("K", 435, 770);
    }

    // Draw the score, combo, and accuracy on the screen
    public void drawScore(Graphics2D g2d, int score, int combo, double accuracy) {
        g2d.setPaint(Color.black);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Score", 530, 50);
        // Format the score to be 7 digits with leading zeros if necessary
        String formattedScore = String.format("%07d", score);
        g2d.drawString(formattedScore, 525, 70);
        g2d.drawString("Combo", 530, 100);
        g2d.drawString(String.valueOf(combo), 550, 120);
        g2d.drawString("Accuracy", 520, 140);
        DecimalFormat acc2dp = new DecimalFormat("#.00");
        g2d.drawString(String.valueOf(acc2dp.format(accuracy)), 535, 160);
    }

    // Draw the notes on the screen
    public void drawNotes(Graphics2D g2d, List<Notes> notes) {
        for (Notes note : notes) {
            note.draw(g2d);
        }
    }

    // Draw the sliders on the screen
    public void drawSliders(Graphics2D g2d, List<Sliders> sliders) {
        for (Sliders slider : sliders) {
            slider.draw(g2d);
        }
    }

    // Draw the image corresponding to the type of score (perfect, okay, bad, miss)
    public void drawScoreImage(Graphics g, String scoreType) {
        Image scoreImage = null;

        switch (scoreType) {
            case "perfect":
                scoreImage = perfectScore;
                break;
            case "okay":
                scoreImage = okayScore;
                break;
            case "bad":
                scoreImage = badScore;
                break;
            case "miss":
                scoreImage = miss;
                break;
        }

        if (scoreImage != null) {
            g.drawImage(scoreImage, 250, 500, this);
        }
    }
}
