package Main;

import javax.swing.*;

public class GameFrame extends JFrame {
    GamePanel panel;
    GameFrame(){
        panel = new GamePanel();
        panel.requestFocusInWindow();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(600,800);
        setResizable(false);
        this.add(panel);
        this.pack();
        // sets the window in the middle of the screen
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
