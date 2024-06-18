package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GamePanel extends JPanel {
    // Rectangle Colors
    private Color dRectangleInitialColor = Color.red;
    private Color dRectangleCurrentColor = dRectangleInitialColor;
    private Color fRectangleInitialColor = Color.blue;
    private Color fRectangleCurrentColor = fRectangleInitialColor;
    private Color jRectangleInitialColor = Color.yellow;
    private Color jRectangleCurrentColor = jRectangleInitialColor;
    private Color kRectangleInitialColor = Color.green;
    private Color kRectangleCurrentColor = kRectangleInitialColor;

    // Game Statistics
    private int score;
    private int combo;
    private int playerMaxCombo;
    private double accuracy;
    private int numOfNotes;
    private int noComboScore;
    private int perfectHitCounter;
    private int okayHitCounter;
    private int badHitCounter;
    public static int missCounter;

    // Notes and Sliders
    private List<Notes> notes1stLane;
    private List<Notes> notes2ndLane;
    private List<Notes> notes3rdLane;
    private List<Notes> notes4thLane;
    private List<Sliders> sliders1stLane;
    private List<Sliders> sliders2ndLane;
    private List<Sliders> sliders3rdLane;
    private List<Sliders> sliders4thLane;

    // Key Presses
    private boolean dPressed = false;
    private boolean fPressed = false;
    private boolean jPressed = false;
    private boolean kPressed = false;

    // Sliders
    private int slidersFirstBottomNodeY = 0;

    // Music and Sounds
    private Music hitSound;
    private Music hitSound2;
    private Music hitSound3;
    private Music hitSound4;
    private Music comboBreak;
    private Music swayToMyBeat;

    // Game State
    private long elapsedTime = 0;
    private boolean isPaused = false;
    private boolean isEnded = false;

    // Menus
    private JPanel pauseMenu;
    private JPanel endMenu;
    
    // Game Drawer
    private GameDrawer gameDrawer;

    // Static Variables
    public static String scoreTypeDisplay = "";

    // Beat Map
    private ArrayList<BeatInfo> beatMap;

    private boolean gameRunning = false;
    private long interval = 15_850_000L;
    private Thread gameThread;


    // Constructor for the GamePanel class
    public GamePanel() {
        // Set the preferred size of the panel to 600x800 pixels
        this.setPreferredSize(new Dimension(600, 800));

        // Setup key bindings for user inputs
        setupKeyBindings();

        // Create the pause menu
        createPauseMenu();

        // Make sure the panel can receive focus
        setFocusable(true);

        // Request focus for the panel within its window
        requestFocusInWindow();

        // Initialize game variables
        score = 0;
        combo = 0;
        playerMaxCombo = 0;
        accuracy = 100.00;

        // Initialize lists for notes and sliders in each lane
        notes1stLane = new ArrayList<>();
        notes2ndLane = new ArrayList<>();
        notes3rdLane = new ArrayList<>();
        notes4thLane = new ArrayList<>();
        sliders1stLane = new ArrayList<>();
        sliders2ndLane = new ArrayList<>();
        sliders3rdLane = new ArrayList<>();
        sliders4thLane = new ArrayList<>();

        // Load sound effects for hits and combo breaks
        hitSound = new Music("assets/hitsound.wav");
        hitSound2 = new Music("assets/hitsound.wav");
        hitSound3 = new Music("assets/hitsound.wav");
        hitSound4 = new Music("assets/hitsound.wav");
        comboBreak = new Music("assets/combobreak.wav");

        // Set volume for combo break sound
        comboBreak.setVolume(-10.0f);

        // Load background music
        swayToMyBeat = new Music("assets/swayToMyBeatInCosmos.wav");

        // Set volume for background music
        swayToMyBeat.setVolume(-10.0f);

        // Initialize the game map
        initializeMap();

        startGameTimer();
    }

    // Method to start the game timer
    private synchronized void startGameTimer() {
        // If the timer is already running, stop it first
        if (gameRunning) {
            stopGameTimer();
        }

        // Reset elapsed time
        elapsedTime = 0;

        // Start the game loop in a separate thread to avoid blocking the main thread
        gameRunning = true;
        gameThread = new Thread(() -> {
            long lastTime = System.nanoTime();

            while (gameRunning && !Thread.currentThread().isInterrupted()) {
                long currentTime = System.nanoTime();
                long deltaTime = currentTime - lastTime;

                // If the elapsed time is greater than or equal to the interval
                if (deltaTime >= interval) {
                    lastTime += interval;

                    // Play background music when elapsed time reaches 300 milliseconds
                    if (elapsedTime == 350) {
                        swayToMyBeat.play();
                    }
                    // Stop background music and end the game when elapsed time exceeds 43400 milliseconds
                    else if (elapsedTime >= 43400) {
                        swayToMyBeat.stop();
                        createEndMenu();
                        toggleEndMenu();
                        gameRunning = false; // Stop the loop
                        break;
                    }

                    // If the game is not paused, update the game state
                    if (!isPaused) {
                        // Generate a new note
                        generateNote();

                        // Update game state
                        updateGame();

                        // Increment the elapsed time by 10 milliseconds (10,000,000 nanoseconds)
                        elapsedTime += 10;

                        // Repaint the game panel on the EDT
                        SwingUtilities.invokeLater(() -> repaint());
                    }
                } else {
                    // Sleep for a short period to prevent CPU hogging
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // If the thread is interrupted during sleep, exit the loop
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        gameThread.start();
    }

    private synchronized void stopGameTimer() {
        // Set gameRunning to false to stop the loop
        gameRunning = false;
        if (gameThread != null) {
            gameThread.interrupt();
            try {
                gameThread.join(); // Wait for the thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameThread = null;
        }
    }



    // Method to initialize the maps by adding the beats in order into an arraylist.
    private void initializeMap() {
        beatMap = new ArrayList<>();
        int speed = 10;
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 0));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, speed, Color.PINK), 800));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, speed, Color.PINK), 1600));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, speed, Color.PINK), 1600));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 2400));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, speed, Color.PINK), 3200));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 3900));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, speed, Color.PINK), 4600));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, speed, Color.PINK), 4600));
        beatMap.add(new BeatInfo(3, new Notes(301, 0, 98, 30, speed, Color.PINK), 5200));
        beatMap.add(new BeatInfo(4, new Notes(401, 0, 98, 30, speed, Color.PINK), 5200));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 5400));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, speed, Color.PINK), 6100));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, speed, Color.PINK), 6500));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 6900));
        beatMap.add(new BeatInfo(2, new Notes(201, 0, 98, 30, speed, Color.PINK), 7600));
        beatMap.add(new BeatInfo(1, new Notes(101, 0, 98, 30, speed, Color.PINK), 7800));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, speed, Color.PINK), 8000));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, speed, Color.PINK), 8400));
        beatMap.add(new BeatInfo(2, new Notes(201, 0, 98, 30, speed, Color.PINK), 9100));
        beatMap.add(new BeatInfo(2, new Notes(201, 0, 98, 30, speed, Color.PINK), 9300));
        beatMap.add(new BeatInfo(4, new Sliders(401, -300, 98, 300, speed, Color.PINK), 9500));
        beatMap.add(new BeatInfo(3, new Sliders(301, -300, 98, 300, speed, Color.PINK), 10100));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 10600));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, speed, Color.PINK), 11200));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, speed, Color.PINK), 11200));
        beatMap.add(new BeatInfo(3, new Notes(301, 0, 98, 30, speed, Color.PINK), 11800));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, speed, Color.PINK), 12100));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, speed, Color.PINK), 12500));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, speed, Color.PINK), 12900));
        beatMap.add(new BeatInfo(2, new Notes(201, 0, 98, 30, speed, Color.PINK), 13500));
        beatMap.add(new BeatInfo(2, new Notes(201, 0, 98, 30, speed, Color.PINK), 13700));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 14000));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, speed, Color.PINK), 14800));
        beatMap.add(new BeatInfo(4, new Notes(401, 0, 98, 30, speed, Color.PINK), 15400));
        beatMap.add(new BeatInfo(4, new Notes(401, 0, 98, 30, speed, Color.PINK), 15600));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, speed, Color.PINK), 15800));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, speed, Color.PINK), 15800));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, speed, Color.PINK), 16600));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, speed, Color.PINK), 17200));
        beatMap.add(new BeatInfo(3, new Notes(301, 0, 98, 30, speed, Color.PINK), 17800));
        // silence
        beatMap.add(new BeatInfo(4, new Sliders(401, -800, 98, 800, speed, Color.PINK), 26400));
        beatMap.add(new BeatInfo(3, new Notes(301, 0, 98, 30, speed, Color.PINK), 27600));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, speed, Color.PINK), 27900));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, speed, Color.PINK), 27900));
        beatMap.add(new BeatInfo(1, new Sliders(101, -200, 98, 200, speed, Color.PINK), 28300));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, speed, Color.PINK), 28300));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, speed, Color.PINK), 28700));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, speed, Color.PINK), 29200));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, speed, Color.PINK), 29200));
        beatMap.add(new BeatInfo(1, new Notes(101, 0, 98, 30, speed, Color.PINK), 29600));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, speed, Color.PINK), 29800));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, speed, Color.PINK), 29800));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, speed, Color.PINK), 30200));
        beatMap.add(new BeatInfo(3, new Notes(301, 0, 98, 30, speed, Color.PINK), 30800));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, speed, Color.PINK), 31000));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, speed, Color.PINK), 31300));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, speed, Color.PINK), 31300));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, speed, Color.PINK), 31700));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 32100));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, speed, Color.PINK), 32100));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, speed, Color.PINK), 32800));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 33200));
        beatMap.add(new BeatInfo(2, new Sliders(201, -400, 98, 400, speed, Color.PINK), 33200));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, speed, Color.PINK), 33900));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, speed, Color.PINK), 33900));
        beatMap.add(new BeatInfo(1, new Sliders(101, -200, 98, 200, speed, Color.PINK), 34300));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, speed, Color.PINK), 34300));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, speed, Color.PINK), 34700));
        beatMap.add(new BeatInfo(4, new Sliders(401, -400, 98, 400, speed, Color.PINK), 34700));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 35500));
        beatMap.add(new BeatInfo(1, new Notes(101, 0, 98, 30, speed, Color.PINK), 36300));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, speed, Color.PINK), 36500));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, speed, Color.PINK), 36900));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, speed, Color.PINK), 36900));
        beatMap.add(new BeatInfo(1, new Sliders(101, -200, 98, 200, speed, Color.PINK), 37300));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, speed, Color.PINK), 37300));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, speed, Color.PINK), 37700));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, speed, Color.PINK), 37700));
        beatMap.add(new BeatInfo(1, new Sliders(101, -200, 98, 200, speed, Color.PINK), 38100));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, speed, Color.PINK), 38500));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, speed, Color.PINK), 38500));
        beatMap.add(new BeatInfo(2, new Notes(201, 0, 98, 30, speed, Color.PINK), 38900));
        beatMap.add(new BeatInfo(4, new Notes(401, 0, 98, 30, speed, Color.PINK), 38900));
        beatMap.add(new BeatInfo(4, new Notes(401, 0, 98, 30, speed, Color.PINK), 39100));
        beatMap.add(new BeatInfo(2, new Sliders(201, -200, 98, 200, speed, Color.PINK), 39300));
        beatMap.add(new BeatInfo(3, new Sliders(301, -200, 98, 200, speed, Color.PINK), 39300));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 39700));
        beatMap.add(new BeatInfo(2, new Notes(201, 0, 98, 30, speed, Color.PINK), 40300));
        beatMap.add(new BeatInfo(1, new Sliders(101, -700, 98, 700, speed, Color.PINK), 40500));
        beatMap.add(new BeatInfo(3, new Sliders(301, -400, 98, 400, speed, Color.PINK), 40800));
        beatMap.add(new BeatInfo(2, new Notes(201, 0, 98, 30, speed, Color.PINK), 41400));
        beatMap.add(new BeatInfo(4, new Sliders(401, -200, 98, 200, speed, Color.PINK), 41600));
        beatMap.add(new BeatInfo(1, new Sliders(101, -400, 98, 400, speed, Color.PINK), 42000));
    }

    // Method to generate notes based on the current elapsed time
    private void generateNote() {
        // Iterate through the beat map to find notes scheduled for the current elapsed time
        for (BeatInfo beatInfo : beatMap) {
            // Check if the timing of the beatInfo matches the current elapsed time
            if (beatInfo.getTiming() == elapsedTime) {
                // Get the lane of the beatInfo
                int lane = beatInfo.getLane();

                // Determine the type of note or slider and add it to the corresponding lane list
                switch (lane) {
                    case 1:
                        if (beatInfo.getNoteType() instanceof Sliders) {
                            sliders1stLane.add((Sliders) beatInfo.getNoteType());
                        } else if (beatInfo.getNoteType() instanceof Notes) {
                            notes1stLane.add((Notes) beatInfo.getNoteType());
                        }
                        break;
                    case 2:
                        if (beatInfo.getNoteType() instanceof Sliders) {
                            sliders2ndLane.add((Sliders) beatInfo.getNoteType());
                        } else if (beatInfo.getNoteType() instanceof Notes) {
                            notes2ndLane.add((Notes) beatInfo.getNoteType());
                        }
                        break;
                    case 3:
                        if (beatInfo.getNoteType() instanceof Sliders) {
                            sliders3rdLane.add((Sliders) beatInfo.getNoteType());
                        } else if (beatInfo.getNoteType() instanceof Notes) {
                            notes3rdLane.add((Notes) beatInfo.getNoteType());
                        }
                        break;
                    case 4:
                        if (beatInfo.getNoteType() instanceof Sliders) {
                            sliders4thLane.add((Sliders) beatInfo.getNoteType());
                        } else if (beatInfo.getNoteType() instanceof Notes) {
                            notes4thLane.add((Notes) beatInfo.getNoteType());
                        }
                        break;
                }
            }
        }
    }

    // Method to update the game state
    private void updateGame() {
        // Update notes in each lane
        updateNotes(notes1stLane);
        updateNotes(notes2ndLane);
        updateNotes(notes3rdLane);
        updateNotes(notes4thLane);

        // Update sliders in each lane
        updateSliders(sliders1stLane);
        updateSliders(sliders2ndLane);
        updateSliders(sliders3rdLane);
        updateSliders(sliders4thLane);
    }

    // Method to update the accuracy based on the number of correctly hit notes
    private void updateAcc() {
        // Calculate accuracy if there are notes in the game
        if (numOfNotes != 0) {
            accuracy = ((float) noComboScore / (30 * (float) numOfNotes)) * 100;
        } else {
            // If there are no notes, set accuracy to 100%
            accuracy = 100.00;
        }
    }

    // Method to update active notes and remove inactive ones
    private void updateNotes(List<Notes> notes) {
        // Iterate through the list of notes
        Iterator<Notes> iterator = notes.iterator();
        while (iterator.hasNext()) {
            Notes note = iterator.next();
            // Update active notes
            if (note.isActive()) {
                combo = note.update(combo, comboBreak);
            } else {
                // Remove inactive notes and update accuracy
                iterator.remove();
                numOfNotes += 1;
                updateAcc();
            }
        }
    }

    // Method to update active sliders and remove inactive ones
    private void updateSliders(List<Sliders> sliders) {
        // Iterate through the list of sliders
        Iterator<Sliders> iterator = sliders.iterator();
        while (iterator.hasNext()) {
            Sliders slider = iterator.next();
            // Update active sliders
            if (slider.isActive()) {
                combo = slider.update(combo, comboBreak);
            } else {
                // Remove inactive sliders and update accuracy
                iterator.remove();
                numOfNotes += 1;
                updateAcc();
            }
        }
    }


    // Method to paint the game components on the panel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw game elements
        drawGameElements(g2d);

        // Draw the score image
        gameDrawer.drawScoreImage(g, scoreTypeDisplay);
    }

    // Method to draw all game elements
    private void drawGameElements(Graphics2D g2d) {
        // Initialize the GameDrawer
        gameDrawer = new GameDrawer();

        // Set the stroke for drawing lines
        g2d.setStroke(new BasicStroke(2));

        // Draw lanes
        gameDrawer.drawLanes(g2d);

        // Draw score
        gameDrawer.drawScore(g2d, score, combo, accuracy);

        // Draw notes in each lane
        gameDrawer.drawNotes(g2d, notes1stLane);
        gameDrawer.drawNotes(g2d, notes2ndLane);
        gameDrawer.drawNotes(g2d, notes3rdLane);
        gameDrawer.drawNotes(g2d, notes4thLane);

        // Draw sliders in each lane
        gameDrawer.drawSliders(g2d, sliders1stLane);
        gameDrawer.drawSliders(g2d, sliders2ndLane);
        gameDrawer.drawSliders(g2d, sliders3rdLane);
        gameDrawer.drawSliders(g2d, sliders4thLane);

        // Draw rectangles for key bindings
        gameDrawer.drawRectangles(g2d, dRectangleCurrentColor, fRectangleCurrentColor, jRectangleCurrentColor, kRectangleCurrentColor);

        // Draw key bindings
        gameDrawer.drawKeyBinds(g2d);
    }


    // Method to set up key bindings for game control
    private void setupKeyBindings() {
        // Key binding for 'D' key press
        getInputMap().put(KeyStroke.getKeyStroke("D"), "D_Pressed");
        getInputMap().put(KeyStroke.getKeyStroke("released D"), "D_Released");
        getActionMap().put("D_Pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Change color of 'D' rectangle
                dRectangleCurrentColor = Color.decode("#ffa8a3");
                repaint();
                if (!dPressed) {
                    // Play hit sound
                    hitSound.play();
                    if (!sliders1stLane.isEmpty()) {
                        // Update combo and score for notes in the 1st lane
                        slidersFirstBottomNodeY = sliders1stLane.getFirst().getBottomNode().y;
                    }
                    updateScoreAndCombo(notes1stLane);
                    dPressed = true;
                }
            }
        });
        getActionMap().put("D_Released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Restore original color of 'D' rectangle
                dRectangleCurrentColor = dRectangleInitialColor;
                if(dPressed){
                    updateScoreAndComboSlider(sliders1stLane, slidersFirstBottomNodeY);
                }
                dPressed = false;
                repaint();
            }
        });
        // Key binding for 'F' key press
        getInputMap().put(KeyStroke.getKeyStroke("F"), "F_Pressed");
        getInputMap().put(KeyStroke.getKeyStroke("released F"), "F_Released");
        getActionMap().put("F_Pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Change color of 'F' rectangle
                fRectangleCurrentColor = Color.decode("#a3afff");
                repaint();
                if (!fPressed) {
                    // Play hit sound
                    hitSound2.play();
                    if (!sliders2ndLane.isEmpty()) {
                        // Update combo and score for notes in the 2nd lane
                        slidersFirstBottomNodeY = sliders2ndLane.getFirst().getBottomNode().y;
                    }
                    updateScoreAndCombo(notes2ndLane);
                    fPressed = true;
                }
            }
        });
        getActionMap().put("F_Released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Restore original color of 'F' rectangle
                fRectangleCurrentColor = fRectangleInitialColor;
                if(fPressed){
                    updateScoreAndComboSlider(sliders2ndLane, slidersFirstBottomNodeY);
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
                jRectangleCurrentColor = Color.decode("#ffffa3");
                repaint();
                if (!jPressed) {
                    hitSound3.play();
                    if (!sliders3rdLane.isEmpty()) {
                        slidersFirstBottomNodeY = sliders3rdLane.getFirst().getBottomNode().y;
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
                    updateScoreAndComboSlider(sliders3rdLane, slidersFirstBottomNodeY);
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
                kRectangleCurrentColor = Color.decode("#a3ffa3");
                repaint();
                if (!kPressed) {
                    hitSound4.play();
                    if (!sliders4thLane.isEmpty()) {
                        slidersFirstBottomNodeY = sliders4thLane.getFirst().getBottomNode().y;
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
                    updateScoreAndComboSlider(sliders4thLane, slidersFirstBottomNodeY);
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


    // Method to update score and combo for notes
    private void updateScoreAndCombo(List<Notes> notes) {
        if (!notes.isEmpty() && notes.getFirst().getY() > 650) {
            notes.getFirst().setActive(false);
            score += (combo != 0) ? 30*combo : 30;
            noComboScore += 30;
            combo += 1;
            scoreTypeDisplay = "perfect";
            perfectHitCounter += 1;
        } else if(!notes.isEmpty() && notes.getFirst().getY() > 625) {
            notes.getFirst().setActive(false);
            score += (combo != 0) ? 10*combo : 10;
            noComboScore += 10;
            combo += 1;
            scoreTypeDisplay = "okay";
            okayHitCounter += 1;
        } else if(!notes.isEmpty() && notes.getFirst().getY() > 600){
            notes.getFirst().setActive(false);
            score += (combo != 0) ? 5*combo : 5;
            noComboScore += 5;
            combo += 1;
            scoreTypeDisplay = "bad";
            badHitCounter += 1;
        }
        if (combo > playerMaxCombo){
            playerMaxCombo = combo ;
        }
    }

    // Method to update score and combo for sliders
    private void updateScoreAndComboSlider(List<Sliders> sliders, int bottomNodeY) {
        if(!sliders.isEmpty()) {
            int sliderLane = sliders.getFirst().getX() / 100;
            int topNodeY = sliders.getFirst().getTopNode().y;
            if (topNodeY > 650 && bottomNodeY < 730 && topNodeY < 730) {
                sliders.getFirst().setActive(false);
                switch (sliderLane){
                    case 1:
                        hitSound.play();
                        break;
                    case 2:
                        hitSound2.play();
                        break;
                    case 3:
                        hitSound3.play();
                        break;
                    case 4:
                        hitSound4.play();
                        break;
                }
                score += (combo != 0) ? 30 * combo : 30;
                noComboScore += 30;
                scoreTypeDisplay = "perfect";
                combo += 1;
                perfectHitCounter += 1;
            } else if (topNodeY > 625 && bottomNodeY < 730 && topNodeY < 730) {
                sliders.getFirst().setActive(false);
                switch (sliderLane){
                    case 1:
                        hitSound.play();
                        break;
                    case 2:
                        hitSound2.play();
                        break;
                    case 3:
                        hitSound3.play();
                        break;
                    case 4:
                        hitSound4.play();
                        break;
                }
                score += (combo != 0) ? 10 * combo : 10;
                noComboScore += 10;
                scoreTypeDisplay = "okay";
                combo += 1;
                okayHitCounter += 1;
            } else if (topNodeY > 600 && bottomNodeY < 730 && topNodeY < 730) {
                sliders.getFirst().setActive(false);
                switch (sliderLane){
                    case 1:
                        hitSound.play();
                        break;
                    case 2:
                        hitSound2.play();
                        break;
                    case 3:
                        hitSound3.play();
                        break;
                    case 4:
                        hitSound4.play();
                        break;
                }
                score += (combo != 0) ? 5 * combo : 5;
                noComboScore += 5;
                scoreTypeDisplay = "bad";
                combo += 1;
                badHitCounter += 1;
            } else if (topNodeY > 650 && bottomNodeY < 750 && topNodeY < 730) {
                sliders.getFirst().setActive(false);
                switch (sliderLane){
                    case 1:
                        hitSound.play();
                        break;
                    case 2:
                        hitSound2.play();
                        break;
                    case 3:
                        hitSound3.play();
                        break;
                    case 4:
                        hitSound4.play();
                        break;
                }
                score += (combo != 0) ? 10 * combo : 10;
                noComboScore += 10;
                scoreTypeDisplay = "okay";
                combo += 1;
                okayHitCounter += 1;
            } else if (topNodeY > 650 && bottomNodeY < 800 && topNodeY < 730) {
                sliders.getFirst().setActive(false);
                switch (sliderLane){
                    case 1:
                        hitSound.play();
                        break;
                    case 2:
                        hitSound2.play();
                        break;
                    case 3:
                        hitSound3.play();
                        break;
                    case 4:
                        hitSound4.play();
                        break;
                }
                score += (combo != 0) ? 5 * combo : 5;
                noComboScore += 5;
                scoreTypeDisplay = "bad";
                combo += 1;
                badHitCounter += 1;
            } else if (topNodeY > 625 && bottomNodeY < 750 && topNodeY < 730) {
                sliders.getFirst().setActive(false);
                switch (sliderLane){
                    case 1:
                        hitSound.play();
                        break;
                    case 2:
                        hitSound2.play();
                        break;
                    case 3:
                        hitSound3.play();
                        break;
                    case 4:
                        hitSound4.play();
                        break;
                }
                score += (combo != 0) ? 10 * combo : 10;
                noComboScore += 10;
                scoreTypeDisplay = "okay";
                combo += 1;
                okayHitCounter += 1;
            } else if (topNodeY > 625 && bottomNodeY < 800 && topNodeY < 730) {
                sliders.getFirst().setActive(false);
                switch (sliderLane){
                    case 1:
                        hitSound.play();
                        break;
                    case 2:
                        hitSound2.play();
                        break;
                    case 3:
                        hitSound3.play();
                        break;
                    case 4:
                        hitSound4.play();
                        break;
                }
                score += (combo != 0) ? 5 * combo : 5;
                noComboScore += 5;
                scoreTypeDisplay = "bad";
                combo += 1;
                badHitCounter += 1;
            } else if (topNodeY > 600 && bottomNodeY < 750 && topNodeY < 730) {
                sliders.getFirst().setActive(false);
                switch (sliderLane){
                    case 1:
                        hitSound.play();
                        break;
                    case 2:
                        hitSound2.play();
                        break;
                    case 3:
                        hitSound3.play();
                        break;
                    case 4:
                        hitSound4.play();
                        break;
                }
                score += (combo != 0) ? 10 * combo : 10;
                noComboScore += 10;
                scoreTypeDisplay = "okay";
                combo += 1;
                okayHitCounter += 1;
            } else if (topNodeY > 600 && bottomNodeY < 800 && topNodeY < 730) {
                sliders.getFirst().setActive(false);
                switch (sliderLane){
                    case 1:
                        hitSound.play();
                        break;
                    case 2:
                        hitSound2.play();
                        break;
                    case 3:
                        hitSound3.play();
                        break;
                    case 4:
                        hitSound4.play();
                        break;
                }
                score += (combo != 0) ? 5 * combo : 5;
                noComboScore += 5;
                scoreTypeDisplay = "bad";
                combo += 1;
                badHitCounter += 1;
            } 
        }
        if (combo > playerMaxCombo){
            playerMaxCombo = combo ;
        }
    }


    // Method to toggle the pause state
    private void togglePause() {
        // Inverse the isPaused state
        isPaused = !isPaused;
        // Set the visibility to true or false depending on the pause state
        pauseMenu.setVisible(isPaused);
        if(!isPaused){
            // Resume the song if it's not paused
            swayToMyBeat.resume();
        } else{
            // Pause the song when the game is paused
            swayToMyBeat.pause();
        }
    }

    // Method for the pause menu
    private void createPauseMenu() {
        pauseMenu = new JPanel();
        pauseMenu.setLayout(new GridBagLayout());
        pauseMenu.setBackground(new Color(0, 0, 0, 150));

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
        // First column
        gbc.gridx = 0;
        // Add some padding
        gbc.insets = new Insets(10, 10, 10, 10);
        // Fill the horizontal space of their cell
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Centers the button within the space
        gbc.anchor = GridBagConstraints.CENTER;
        // Put resume button in the first row
        gbc.gridy = 0;
        pauseMenu.add(resumeButton, gbc);
        // Put restart button in the second row
        gbc.gridy = 1;
        pauseMenu.add(restartButton, gbc);
        // Put quit button in the third row
        gbc.gridy = 2;
        pauseMenu.add(quitButton, gbc);

        pauseMenu.setVisible(false);
        this.setLayout(new BorderLayout());
        // Add the pause menu panel to the center of the JPanel
        this.add(pauseMenu, BorderLayout.CENTER);
    }

    // Method to toggle the end menu
    private void toggleEndMenu(){
        // Inverse the state of isEnded
        isEnded = !isEnded;
        // Set visibility to true or false
        endMenu.setVisible(isEnded);
    }

    // Method for the end menu
    private void createEndMenu(){
        endMenu = new JPanel();
        endMenu.setBackground(Color.WHITE);
        endMenu.setVisible(false);
        this.setLayout(new BorderLayout());
        this.add(endMenu, BorderLayout.CENTER);

        endMenu.setLayout(new BorderLayout());

        // For title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#2E2E2E"));
        titlePanel.setPreferredSize(new Dimension(600,100));

        // Adding margin to the titlePanel
        int topMargin = 25;
        int leftMargin = 10;
        int bottomMargin = 10;
        int rightMargin = 10;
        titlePanel.setBorder(BorderFactory.createEmptyBorder(topMargin, leftMargin, bottomMargin, rightMargin));

        JLabel songTitle = new JLabel("HOYO-MiX feat. Robin (VO:Chevy) - Sway to My Beat in Cosmos");
        songTitle.setForeground(Color.white);
        songTitle.setFont(songTitle.getFont().deriveFont(16f));
        titlePanel.add(songTitle);

        // JLabel to display what time the user play the game
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateAndTime = dateFormat.format(new Date());
        JLabel playedOn = new JLabel("Played on " + currentDateAndTime);
        playedOn.setForeground(Color.white);
        playedOn.setFont(playedOn.getFont().deriveFont(12f));
        titlePanel.add(playedOn);

        // JPanel for scoreboard
        JPanel scoreBoard = new JPanel();
        scoreBoard.setLayout(new GridLayout(8,1));
        scoreBoard.setBackground(Color.decode("#391456"));
        // JLabel for score text
        String formattedScore = String.format("%07d", score);
        JLabel scoreText = new JLabel("Score: " + formattedScore);
        scoreText.setFont(scoreText.getFont().deriveFont(16f));
        scoreText.setForeground(Color.white);
        scoreText.setHorizontalAlignment(SwingConstants.CENTER);

        // JLabel for accuracy
        DecimalFormat acc2dp = new DecimalFormat("#.00");
        JLabel accuracyText = new JLabel("Accuracy: " + acc2dp.format(accuracy));
        accuracyText.setFont(accuracyText.getFont().deriveFont(16f));
        accuracyText.setForeground(Color.white);
        accuracyText.setHorizontalAlignment(SwingConstants.CENTER);
        //JLabel for perfect hit amount
        JLabel perfectHitAmountText = new JLabel("Perfect Hit amount (30): " + perfectHitCounter + "x");
        perfectHitAmountText.setFont(accuracyText.getFont().deriveFont(16f));
        perfectHitAmountText.setForeground(Color.white);
        perfectHitAmountText.setHorizontalAlignment(SwingConstants.CENTER);
        //JLabel for okay hit amount
        JLabel okayHitAmountText = new JLabel("Okay Hit amount (10): " + okayHitCounter + "x");
        okayHitAmountText.setFont(accuracyText.getFont().deriveFont(16f));
        okayHitAmountText.setForeground(Color.white);
        okayHitAmountText.setHorizontalAlignment(SwingConstants.CENTER);
        //JLabel for bad hit amount
        JLabel badHitAmountText = new JLabel("Bad Hit amount (5): " + badHitCounter + "x");
        badHitAmountText.setFont(accuracyText.getFont().deriveFont(16f));
        badHitAmountText.setForeground(Color.white);
        badHitAmountText.setHorizontalAlignment(SwingConstants.CENTER);
        //JLabel for miss amount
        JLabel missAmountText = new JLabel("Miss amount: " + missCounter + "x");
        missAmountText.setFont(accuracyText.getFont().deriveFont(16f));
        missAmountText.setForeground(Color.white);
        missAmountText.setHorizontalAlignment(SwingConstants.CENTER);
        //JLabel for highest combo the user got
        JLabel HighestCombo = new JLabel("Highest Combo: " + playerMaxCombo + "x");
        HighestCombo.setFont(accuracyText.getFont().deriveFont(16f));
        HighestCombo.setForeground(Color.white);
        HighestCombo.setHorizontalAlignment(SwingConstants.CENTER);


        scoreBoard.add(scoreText);
        scoreBoard.add(perfectHitAmountText);
        scoreBoard.add(okayHitAmountText);
        scoreBoard.add(badHitAmountText);
        scoreBoard.add(missAmountText);
        scoreBoard.add(accuracyText);
        scoreBoard.add(HighestCombo);

        // Panel for retry and quit button
        JPanel buttonContainer = new JPanel();
        buttonContainer.setPreferredSize(new Dimension(600,60));
        buttonContainer.setBackground(Color.decode("#2E2E2E"));
        JButton retryButton = new JButton("Retry");
        retryButton.addActionListener(e -> restartGame());
        retryButton.setPreferredSize(new Dimension(150, 50));
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> quitGame());
        quitButton.setPreferredSize(new Dimension(150, 50));


        buttonContainer.add(retryButton);
        buttonContainer.add(quitButton);

        endMenu.add(buttonContainer, BorderLayout.SOUTH);
        endMenu.add(scoreBoard, BorderLayout.CENTER);
        endMenu.add(titlePanel, BorderLayout.NORTH);
    }

    // Method to resume the game after it was paused
    private void resumeGame() {
        isPaused = false;
        pauseMenu.setVisible(false);
        swayToMyBeat.resume();
    }

    private void restartGame() {
        // Reset the game state variables
        isPaused = false;
        if (isEnded) {
            toggleEndMenu();
        }
        pauseMenu.setVisible(false);
        score = 0;
        combo = 0;
        playerMaxCombo = 0;
        noComboScore = 0;
        numOfNotes = 0;
        okayHitCounter = 0;
        perfectHitCounter = 0;
        missCounter = 0;
        badHitCounter = 0;
        accuracy = 100.00;
        sliders1stLane = new ArrayList<>();
        sliders2ndLane = new ArrayList<>();
        sliders3rdLane = new ArrayList<>();
        sliders4thLane = new ArrayList<>();
        notes1stLane = new ArrayList<>();
        notes2ndLane = new ArrayList<>();
        notes3rdLane = new ArrayList<>();
        notes4thLane = new ArrayList<>();
        scoreTypeDisplay = "";
        initializeMap();

        // Stop the existing timer
        stopGameTimer();

        // Reset elapsed time and start a new game timer
        elapsedTime = 0;
        startGameTimer();
    }


    // Terminate the program
    private void quitGame() {
        System.exit(0);
    }
}
