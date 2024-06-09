package Main;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Music {
    private Clip clip;
    private FloatControl volumeControl;
    public Long currentFrame;
    private AudioInputStream audioStream;
    private boolean isPlaying;

    Music(String filePath) {
        try {
            File audioFile = new File(filePath);
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            currentFrame = 0L;
            isPlaying = false;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Method to play the clip from the beginning
    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();  // Stop the clip if it is currently running
            }
            clip.setMicrosecondPosition(0); // Rewind to the beginning
            clip.start(); // Start playing
            isPlaying = true;
        }
    }

    // Method to pause the clip
    public void pause() {
        if (clip != null && isPlaying) {
            currentFrame = clip.getMicrosecondPosition();
            clip.stop();
            isPlaying = false;
        }
    }

    // Method to resume the clip from where it was paused
    public void resume() {
        if (clip != null && !isPlaying) {
            clip.setMicrosecondPosition(currentFrame);
            clip.start();
            isPlaying = true;
        }
    }

    // Method to stop the clip
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.setMicrosecondPosition(0);
            isPlaying = false;
        }
    }

    // Method to set the volume of the clip
    public void setVolume(float volume) {
        if (volumeControl != null) {
            volumeControl.setValue(volume);
        }
    }

    // Method to play the clip from a specific time
    public void playFrom(long milliseconds) {
        if (clip != null) {
            stop(); // Stop the clip before setting the position
            clip.setMicrosecondPosition(milliseconds * 1000); // Set position in microseconds
            clip.start(); // Start playing from the specified time
            isPlaying = true;
        }
    }
}
