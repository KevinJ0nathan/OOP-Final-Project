package Main;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 * This class is responsible for handling audio playback
 * It supports operations such as play, pause, resume, stop, and volume control.
 */
public class Music {
    // Clip for audio playback
    private Clip clip;
    // Control for adjusting the volume
    private FloatControl volumeControl;
    // The current frame position of the audio
    public Long currentFrame;
    // The AudioInputStream to read the audio file
    private AudioInputStream audioStream;
    // Flag to check if the audio is currently playing
    private boolean isPlaying;

    /**
     * Construct a music object with the given audio file path
     *
     * @param filePath The path to the audio file.
     */
    Music(String filePath) {
        try {
            // Audio file
            File audioFile = new File(filePath);
            // Get input stream for audio
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            // Get clip for playback
            clip = AudioSystem.getClip();
            // Open audio stream
            clip.open(audioStream);
            // For controlling volume
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // Initialize position
            currentFrame = 0L;
            isPlaying = false;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // error handling
            e.printStackTrace();
        }
    }

    // Method to play the audio from the beginning
    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                // Stop the clip if it is currently running
                clip.stop();
            }
            // Rewind to the beginning
            clip.setMicrosecondPosition(0);
            // Start playing
            clip.start();
            isPlaying = true;
        }
    }

    // Method to pause the audio
    public void pause() {
        if (clip != null && isPlaying) {
            // Save current position of the audio
            currentFrame = clip.getMicrosecondPosition();
            // Pause the audio
            clip.stop();
            isPlaying = false;
        }
    }

    // Method to resume the audio from where it was paused
    public void resume() {
        if (clip != null && !isPlaying) {
            // set position of the audio to when it was paused
            clip.setMicrosecondPosition(currentFrame);
            // Resume the audio
            clip.start();
            isPlaying = true;
        }
    }

    // Method to stop the audio
    public void stop() {
        if (clip != null) {
            // Stop audio
            clip.stop();
            // Reset position of the audio
            clip.setMicrosecondPosition(0);
            isPlaying = false;
        }
    }

    /** Method to set the volume of the clip
     *
     * @param volume
     */
    public void setVolume(float volume) {
        if (volumeControl != null) {
            // Set volume
            volumeControl.setValue(volume);
        }
    }

    /**
     * Starts playing the audio from a specific time.
     *
     * @param milliseconds The time to start playing from, in milliseconds.
     */
    public void playFrom(long milliseconds) {
        if (clip != null) {
            // Stop audio before setting position
            stop();
            // Set position in microseconds
            clip.setMicrosecondPosition(milliseconds * 1000);
            // Start playing from the specified time
            clip.start();
            isPlaying = true;
        }
    }
}
