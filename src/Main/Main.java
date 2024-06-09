package Main;

import javax.swing.*;

public class Main extends JFrame {
    public Main(){
        setTitle("Main Menu");
        setSize(600,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton PlayButton = new JButton("Play");
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}