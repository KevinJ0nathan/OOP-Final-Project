package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GamePanel extends JPanel {
    private Color dRectangleInitialColor = Color.red;
    private Color dRectangleCurrentColor = dRectangleInitialColor;
    private Color fRectangleInitialColor = Color.blue;
    private Color fRectangleCurrentColor = fRectangleInitialColor;
    private Color jRectangleInitialColor = Color.yellow;
    private Color jRectangleCurrentColor = jRectangleInitialColor;
    private Color kRectangleInitialColor = Color.green;
    private Color kRectangleCurrentColor = kRectangleInitialColor;

    private int score;
    private int highScore;
    private int combo;
    private double accuracy;
    private int numOfNotes;
    private int noComboScore;

    private List<Notes> notes1stLane;
    private List<Notes> notes2ndLane;
    private List<Notes> notes3rdLane;
    private List<Notes> notes4thLane;
    private List<Sliders> sliders1stLane;
    private List<Sliders> sliders2ndLane;
    private List<Sliders> sliders3rdLane;
    private List<Sliders> sliders4thLane;

    private boolean dPressed = false;
    private boolean fPressed = false;
    private boolean jPressed = false;
    private boolean kPressed = false;

    private int temp = 0;

    private Music hitSound;
    private Music comboBreak;
    private Music swayToMyBeat;

    private int elapsedTime;
    private boolean isPaused = false;
    private JPanel pauseMenu;
    private Timer gameTimer;


    private ArrayList<BeatInfo> beatMap;



    public GamePanel() {
        this.setPreferredSize(new Dimension(600, 800));
        setupKeyBindings();
        createPauseMenu();
        setFocusable(true); // Ensure panel can receive focus
        requestFocusInWindow();
        score = 0;
        highScore = 0;
        combo = 0;
        accuracy = 100.00;
        notes1stLane = new ArrayList<>();
        notes2ndLane = new ArrayList<>();
        notes3rdLane = new ArrayList<>();
        notes4thLane = new ArrayList<>();
        sliders1stLane = new ArrayList<>();
        sliders2ndLane = new ArrayList<>();
        sliders3rdLane = new ArrayList<>();
        sliders4thLane = new ArrayList<>();
        hitSound = new Music("assets/hitsound.wav");
        comboBreak = new Music("assets/combobreak.wav");
        comboBreak.setVolume(-10.0f);
        swayToMyBeat = new Music("assets/swayToMyBeatInCosmos.wav");
        swayToMyBeat.setVolume(-10.0f); // Set the volume lower
        // Timer to generate new notes based on the elapsed time
        initializeMap();
        gameTimer = new Timer(10, e -> {
            if (elapsedTime == 400) { //
                swayToMyBeat.play();
            } else if(elapsedTime == 43400){
                swayToMyBeat.stop();
            }
            if(!isPaused) {
                generateNote();
                updateGame();
                repaint();
                elapsedTime += 10; // Increment elapsed time by the timer's delay
            }
        });
        gameTimer.start();
    }

    private void initializeMap() {
        beatMap = new ArrayList<>();
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 0));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, 10, Color.PINK), 800));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, 10, Color.PINK), 1600));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, 10, Color.PINK), 1600));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 2400));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, 10, Color.PINK), 3200));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 3900));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, 10, Color.PINK), 4600));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, 10, Color.PINK), 4600));
        beatMap.add(new BeatInfo(3, new Notes(301,0,98,30,10,Color.PINK),5200));
        beatMap.add(new BeatInfo(4, new Notes(401,0,98,30,10,Color.PINK),5200));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 5400));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, 10, Color.PINK), 6100));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, 10, Color.PINK), 6500));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 6900));
        beatMap.add(new BeatInfo(2, new Notes(201,0,98,30,10,Color.PINK),7700));
        beatMap.add(new BeatInfo(1, new Notes(101,0,98,30,10,Color.PINK),7900));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, 10, Color.PINK), 8100));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, 10, Color.PINK), 8500));
        beatMap.add(new BeatInfo(2, new Notes(201,0,98,30,10,Color.PINK),9200));
        beatMap.add(new BeatInfo(2, new Notes(201,0,98,30,10,Color.PINK),9400));
        beatMap.add(new BeatInfo(4, new Sliders(401, -300, 98, 300, 10, Color.PINK), 9600));
        beatMap.add(new BeatInfo(3, new Sliders(301, -300, 98, 300, 10, Color.PINK), 10200));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 10700));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, 10, Color.PINK), 11300));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, 10, Color.PINK), 11300));
        beatMap.add(new BeatInfo(3, new Notes(301,0,98,30,10,Color.PINK),11900));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, 10, Color.PINK), 12300));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, 10, Color.PINK), 12700));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, 10, Color.PINK), 13000));
        beatMap.add(new BeatInfo(2, new Notes(201,0,98,30,10,Color.PINK),13800));
        beatMap.add(new BeatInfo(2, new Notes(201,0,98,30,10,Color.PINK),14000));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 14200));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, 10, Color.PINK), 14800));
        beatMap.add(new BeatInfo(4, new Notes(401,0,98,30,10,Color.PINK),15400));
        beatMap.add(new BeatInfo(4, new Notes(401,0,98,30,10,Color.PINK),15600));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, 10, Color.PINK), 15800));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, 10, Color.PINK), 15800));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, 10, Color.PINK), 16600));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, 10, Color.PINK), 17200));
        beatMap.add(new BeatInfo(3, new Notes(301,0,98,30,10,Color.PINK),17800));
        beatMap.add(new BeatInfo(4, new Sliders(401, -800, 98, 800, 10, Color.PINK), 26450));
        beatMap.add(new BeatInfo(3, new Notes(301,0,98,30,10,Color.PINK),27700));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, 10, Color.PINK), 27900));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, 10, Color.PINK), 27900));
        beatMap.add(new BeatInfo(1, new Sliders(101, -200, 98, 200, 10, Color.PINK), 28300));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, 10, Color.PINK), 28300));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, 10, Color.PINK), 28700));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, 10, Color.PINK), 29300));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, 10, Color.PINK), 29300));
        beatMap.add(new BeatInfo(1, new Notes(101, 0, 98, 30, 10, Color.PINK), 29700));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, 10, Color.PINK), 29900));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, 10, Color.PINK), 29900));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, 10, Color.PINK), 30300));
        beatMap.add(new BeatInfo(3, new Notes(301, 0, 98, 30, 10, Color.PINK), 30900));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, 10, Color.PINK), 31100));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, 10, Color.PINK), 31500));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, 10, Color.PINK), 31500));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, 10, Color.PINK), 31900));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 32300));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, 10, Color.PINK), 32300));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, 10, Color.PINK), 32900));
        beatMap.add(new BeatInfo(4, new Notes(401, 0, 98, 30, 10, Color.PINK), 33300));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 33500));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, 10, Color.PINK), 33500));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, 10, Color.PINK), 34100));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, 10, Color.PINK), 34100));
        beatMap.add(new BeatInfo(1, new Sliders(101, -200, 98, 200, 10, Color.PINK), 34500));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, 10, Color.PINK), 34500));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, 10, Color.PINK), 34900));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, 10, Color.PINK), 34900));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 35500));
        beatMap.add(new BeatInfo(1, new Notes(101, 0, 98, 30, 10, Color.PINK), 36200));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, 10, Color.PINK), 36400));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, 10, Color.PINK), 36800));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, 10, Color.PINK), 36800));
        beatMap.add(new BeatInfo(1, new Sliders(101, -200, 98, 200, 10, Color.PINK), 37200));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, 10, Color.PINK), 37200));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, 10, Color.PINK), 37600));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, 10, Color.PINK), 37600));
        beatMap.add(new BeatInfo(1, new Sliders(101, -200, 98, 200, 10, Color.PINK), 38000));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, 10, Color.PINK), 38400));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, 10, Color.PINK), 38400));
        beatMap.add(new BeatInfo(2, new Notes(201, 0, 98, 30, 10, Color.PINK), 38800));
        beatMap.add(new BeatInfo(4, new Notes(401, 0, 98, 30, 10, Color.PINK), 38800));
        beatMap.add(new BeatInfo(4, new Notes(401, 0, 98, 30, 10, Color.PINK), 39000));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, 10, Color.PINK), 39200));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, 10, Color.PINK), 39200));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 39600));
        beatMap.add(new BeatInfo(2, new Notes(201, 0, 98, 30, 10, Color.PINK), 40200));
        beatMap.add(new BeatInfo(1, new Sliders(101, -700, 98, 700, 10, Color.PINK), 40400));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, 10, Color.PINK), 40700));
        beatMap.add(new BeatInfo(2, new Notes(201, 0, 98, 30, 10, Color.PINK), 41300));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, 10, Color.PINK), 41500));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, 10, Color.PINK), 41900));
    }

    private void generateNote() {
        for(BeatInfo beatInfo: beatMap){
            if(beatInfo.getTiming() == elapsedTime){
                int lane = beatInfo.getLane();
                switch (lane){
                    case 1:
                        if(beatInfo.getNoteType() instanceof Sliders){
                            sliders1stLane.add((Sliders) beatInfo.getNoteType());
                        } else if(beatInfo.getNoteType() instanceof Notes){
                            notes1stLane.add((Notes) beatInfo.getNoteType());
                        }
                        break;
                    case 2:
                        if(beatInfo.getNoteType() instanceof Sliders){
                            sliders2ndLane.add((Sliders) beatInfo.getNoteType());
                        } else if(beatInfo.getNoteType() instanceof Notes){
                            notes2ndLane.add((Notes) beatInfo.getNoteType());
                        }
                        break;
                    case 3:
                        if(beatInfo.getNoteType() instanceof Sliders){
                            sliders3rdLane.add((Sliders) beatInfo.getNoteType());
                        } else if(beatInfo.getNoteType() instanceof Notes){
                            notes3rdLane.add((Notes) beatInfo.getNoteType());
                        }
                        break;
                    case 4:
                        if(beatInfo.getNoteType() instanceof Sliders){
                            sliders4thLane.add((Sliders) beatInfo.getNoteType());
                        } else if(beatInfo.getNoteType() instanceof Notes){
                            notes4thLane.add((Notes) beatInfo.getNoteType());
                        }
                        break;
            }}
        }

    }

    private void updateGame() {
        updateNotes(notes1stLane);
        updateNotes(notes2ndLane);
        updateNotes(notes3rdLane);
        updateNotes(notes4thLane);
        updateSliders(sliders1stLane);
        updateSliders(sliders2ndLane);
        updateSliders(sliders3rdLane);
        updateSliders(sliders4thLane);
    }

    private void updateAcc(){
        if(numOfNotes != 0) {
            accuracy = ((float) noComboScore / (30 * (float) numOfNotes)) * 100;
        } else{accuracy = 100.00;}
    }

    private void updateNotes(List<Notes> notes) {
        Iterator<Notes> iterator = notes.iterator();
        while (iterator.hasNext()) {
            Notes note = iterator.next();
            if (note.isActive()) {
                combo = note.update(combo, comboBreak);
            }
            else {
                iterator.remove();
                numOfNotes += 1;
                updateAcc();
            }
        }
    }

    private void updateSliders(List<Sliders> sliders) {
        Iterator<Sliders> iterator = sliders.iterator();
        while (iterator.hasNext()) {
            Sliders slider = iterator.next();
            if (slider.isActive()) {
                combo = slider.update(combo, comboBreak);
            }
            else {
                iterator.remove();
                numOfNotes += 1;
                updateAcc();
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawGameElements(g2d);
        if(dPressed) {
            drawLights(g,101, 700, 200, 600);
        } else if(fPressed){
            drawLights(g,201, 700, 300, 600);
        } else if(jPressed){
            drawLights(g,301, 700, 400, 600);
        } else if(kPressed){
            drawLights(g,401, 700, 500, 600);
        }
    }

    private void drawGameElements(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));
        drawLanes(g2d);
        drawScore(g2d);
        drawNotes(g2d, notes1stLane);
        drawNotes(g2d, notes2ndLane);
        drawNotes(g2d, notes3rdLane);
        drawNotes(g2d, notes4thLane);
        drawSliders(g2d, sliders1stLane);
        drawSliders(g2d, sliders2ndLane);
        drawSliders(g2d, sliders3rdLane);
        drawSliders(g2d, sliders4thLane);
        drawRectangles(g2d);
        drawKeyBinds(g2d);
    }

    private void drawLanes(Graphics2D g2d) {
        g2d.drawLine(100, 0, 100, 800);
        g2d.drawLine(200, 0, 200, 800);
        g2d.drawLine(300, 0, 300, 800);
        g2d.drawLine(400, 0, 400, 800);
        g2d.drawLine(500, 0, 500, 800);
    }

    private void drawRectangles(Graphics2D g2d) {
        g2d.setPaint(dRectangleCurrentColor);
        g2d.fillRect(101, 700, 98, 100);
        g2d.setPaint(fRectangleCurrentColor);
        g2d.fillRect(201, 700, 98, 100);
        g2d.setPaint(jRectangleCurrentColor);
        g2d.fillRect(301, 700, 98, 100);
        g2d.setPaint(kRectangleCurrentColor);
        g2d.fillRect(401, 700, 98, 100);
    }

    private void drawLights(Graphics g, int startX, int startY, int endX, int endY) {
        Graphics2D g2d = (Graphics2D) g;

        // Define the colors for the gradient
        Color startColor = new Color(255, 255, 255, 160);
        Color endColor = new Color(5, 5, 5, 36);

        // Create a GradientPaint object
        GradientPaint gradient = new GradientPaint(startX, startY, startColor, endX, endY, endColor);

        // Set the paint to the gradient
        g2d.setPaint(gradient);

        // Fill a rectangle with the gradient
        g2d.fillRect(startX, endY, endX - startX, startY - endY);
    }


    private void drawKeyBinds(Graphics2D g2d) {
        g2d.setPaint(Color.black);
        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        g2d.drawString("D", 135, 770);
        g2d.drawString("F", 235, 770);
        g2d.drawString("J", 335, 770);
        g2d.drawString("K", 435, 770);
    }

    private void drawScore(Graphics2D g2d) {
        g2d.setPaint(Color.black);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Score", 530, 50);
        g2d.drawString(String.valueOf(score), 550, 70);
        g2d.drawString("Combo", 530, 90);
        g2d.drawString(String.valueOf(combo), 550, 110);
        g2d.drawString("Accuracy", 530, 140);
        DecimalFormat acc2dp = new DecimalFormat("#.00");
        g2d.drawString(String.valueOf(acc2dp.format(accuracy)), 550, 160);
    }

    private void drawNotes(Graphics2D g2d, List<Notes> notes) {
        for (Notes note : notes) {
            note.draw(g2d);
        }
    }

    private void drawSliders(Graphics2D g2d, List<Sliders> sliders) {
        for (Sliders slider : sliders) {
            slider.draw(g2d);
        }
    }

    private void setupKeyBindings() {
        getInputMap().put(KeyStroke.getKeyStroke("D"), "D_Pressed");
        getInputMap().put(KeyStroke.getKeyStroke("released D"), "D_Released");
        getActionMap().put("D_Pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dRectangleCurrentColor = Color.decode("#ff0d51");
                repaint();
                if (!dPressed) {
                    hitSound.play();
                    if (!sliders1stLane.isEmpty()) {
                        temp = sliders1stLane.getFirst().getBottomNode().y;
                    }
                    updateScoreAndCombo(notes1stLane);
                    dPressed = true;
                }
            }
        });
        getActionMap().put("D_Released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dRectangleCurrentColor = dRectangleInitialColor;
                if(dPressed){
                    updateScoreAndComboSlider(sliders1stLane, temp);
                }
                dPressed = false;
                repaint();
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke("F"), "F_Pressed");
        getInputMap().put(KeyStroke.getKeyStroke("released F"), "F_Released");
        getActionMap().put("F_Pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fRectangleCurrentColor = Color.decode("#5147ff");
                repaint();
                if (!fPressed) {
                    hitSound.play();
                    if (!sliders2ndLane.isEmpty()) {
                        temp = sliders2ndLane.getFirst().getBottomNode().y;
                    }
                    updateScoreAndCombo(notes2ndLane);
                    fPressed = true;
                }
            }
        });
        getActionMap().put("F_Released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fRectangleCurrentColor = fRectangleInitialColor;
                if(fPressed){
                    updateScoreAndComboSlider(sliders2ndLane, temp);
                }
                fPressed = false;
                repaint();
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke("J"), "J_Pressed");
        getInputMap().put(KeyStroke.getKeyStroke("released J"), "J_Released");
        getActionMap().put("J_Pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jRectangleCurrentColor = Color.decode("#faeb64");
                repaint();
                if (!jPressed) {
                    hitSound.play();
                    if (!sliders3rdLane.isEmpty()) {
                        temp = sliders3rdLane.getFirst().getBottomNode().y;
                    }
                    updateScoreAndCombo(notes3rdLane);
                    jPressed = true;
                }
            }
        });
        getActionMap().put("J_Released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jRectangleCurrentColor = jRectangleInitialColor;
                if(jPressed){
                    updateScoreAndComboSlider(sliders3rdLane, temp);
                }
                jPressed = false;
                repaint();
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke("K"), "K_Pressed");
        getInputMap().put(KeyStroke.getKeyStroke("released K"), "K_Released");
        getActionMap().put("K_Pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kRectangleCurrentColor = Color.decode("#62f752");
                repaint();
                if (!kPressed) {
                    hitSound.play();
                    if (!sliders4thLane.isEmpty()) {
                        temp = sliders4thLane.getFirst().getBottomNode().y;
                    }
                    updateScoreAndCombo(notes4thLane);
                    kPressed = true;
                }
            }
        });
        getActionMap().put("K_Released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kRectangleCurrentColor = kRectangleInitialColor;
                if(kPressed){
                    updateScoreAndComboSlider(sliders4thLane, temp);
                }
                kPressed = false;
                repaint();
            }
        });
        // Bind Escape key to togglePause action
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "pause");
        getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });

    }


    private void updateScoreAndCombo(List<Notes> notes) {
        if (!notes.isEmpty() && notes.getFirst().getY() > 650) {
            notes.getFirst().setActive(false);
            score += (combo != 0) ? 30*combo : 30;
            noComboScore += 30;
            combo += 1;
        } else if(!notes.isEmpty() && notes.getFirst().getY() > 625) {
            notes.getFirst().setActive(false);
            score += (combo != 0) ? 10*combo : 10;
            noComboScore += 10;
            combo += 1;
        } else if(!notes.isEmpty() && notes.getFirst().getY() > 600){
            score += (combo != 0) ? 5*combo : 5;
            noComboScore += 5;
            combo += 1;
        }
    }

    private void updateScoreAndComboSlider(List<Sliders> sliders, int bottomNodeY) {
        if (!sliders.isEmpty() && sliders.getFirst().getTopNode().y > 650 && bottomNodeY < 750) {
            sliders.getFirst().setActive(false);
            hitSound.play();
            score += (combo != 0) ? 30*combo : 30;
            noComboScore += 30;
            combo += 1;
        } else if (!sliders.isEmpty() && sliders.getFirst().getTopNode().y > 625 && bottomNodeY < 750) {
            sliders.getFirst().setActive(false);
            hitSound.play();
            score += (combo != 0) ? 10*combo : 10;
            noComboScore += 10;
            combo += 1;
        } else if (!sliders.isEmpty() && sliders.getFirst().getTopNode().y > 600 && bottomNodeY < 750) {
            sliders.getFirst().setActive(false);
            hitSound.play();
            score += (combo != 0) ? 5*combo : 5;
            noComboScore += 5;
            combo += 1;
        }
    }


    private void togglePause() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);
        if(!isPaused){
            swayToMyBeat.resume();
        } else{
            swayToMyBeat.pause();
        }
    }

    private void createPauseMenu() {
        pauseMenu = new JPanel();
        pauseMenu.setLayout(new GridBagLayout());
        pauseMenu.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent background

        // Create buttons
        JButton resumeButton = new JButton("Resume");
        resumeButton.setPreferredSize(new Dimension(150, 50));
        JButton restartButton = new JButton("Restart");
        restartButton.setPreferredSize(new Dimension(150, 50));
        JButton quitButton = new JButton("Quit");
        quitButton.setPreferredSize(new Dimension(150, 50));

        // Add action listeners
        resumeButton.addActionListener(e -> resumeGame());
        restartButton.addActionListener(e -> restartGame());
        quitButton.addActionListener(e -> quitGame());

        // Add buttons to the pause menu panel with GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        pauseMenu.add(resumeButton, gbc);

        gbc.gridy = 1;
        pauseMenu.add(restartButton, gbc);

        gbc.gridy = 2;
        pauseMenu.add(quitButton, gbc);

        pauseMenu.setVisible(false);
        this.setLayout(new BorderLayout());
        this.add(pauseMenu, BorderLayout.CENTER); // Add the pause menu panel to the center of the JPanel
    }

    private void resumeGame() {
        isPaused = false;
        pauseMenu.setVisible(false);
        swayToMyBeat.resume();
    }

    private void restartGame() {
        // Logic to restart the game
        isPaused = false;
        pauseMenu.setVisible(false);
        score = 0;
        highScore = 0;
        combo = 0;
        accuracy = 100.00;
        sliders1stLane = new ArrayList<>();
        sliders2ndLane = new ArrayList<>();
        sliders3rdLane = new ArrayList<>();
        sliders4thLane = new ArrayList<>();
        notes1stLane = new ArrayList<>();
        notes2ndLane = new ArrayList<>();
        notes3rdLane = new ArrayList<>();
        notes4thLane = new ArrayList<>();
        // Stop the existing timer
        gameTimer.stop();
        elapsedTime = 0;

        // Create a new timer with the same properties
        gameTimer = new Timer(10, e -> {
            if (elapsedTime == 400) { //
                swayToMyBeat.play();
            } else if(elapsedTime == 43400){
                swayToMyBeat.stop();
            }
            if (!isPaused) {
                generateNote();
                updateGame();
                repaint();
                elapsedTime += 10; // Increment elapsed time by the timer's delay
            }
        });

        // Start the new timer
        gameTimer.start();
        initializeMap();
    }

    private void quitGame() {

    }
}
