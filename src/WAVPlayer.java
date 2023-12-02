/*
 * project:     COMPUCLICK
 * file:        WAVPlayer.java
 * modified:    2023-12-02
 * name:        Ethan Mentzer
 * lab:         CSC 171-13
 */

/*
 * NOTE: ChatGPT helped me understand how to properly put together the code
 *       in this file.
 */


// Import necessary classes
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.net.URL;
import java.util.Objects;


/**
 * The WAVPlayer class, when a .wav filename is specified on instantiation, makes
 * it easy to play and stop that sound file in the future. Instances can also
 * be told to loop using the built-in "LOOP_CONTINUOUSLY" argument.
 */
public class WAVPlayer {

    // Declare instance variables
    private Clip clip;
    private final boolean loop;
    private final String fileName;

    // Constructor takes filename and whether to loop as arguments
    public WAVPlayer(String fileName, boolean loop) {
        this.fileName = fileName;
        this.loop = loop;
    }

    // Plays the audio
    public void playSound() {
        try {
            URL url = getClass().getResource(fileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(url));

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Set to loop continuously if desired
            if (loop)
                clip.loop(Clip.LOOP_CONTINUOUSLY);

            clip.start();

        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }

    // Stops the audio
    public void stopSound() {
        clip.stop();
    }

}
