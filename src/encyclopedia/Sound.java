/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encyclopedia;

import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Petkovics
 */
public class Sound {

    private InputStream inputStream = null;
    private static AudioStream audioStream = null;

    public static void stop() {
        AudioPlayer.player.stop(audioStream);
    }

    public void play(String FILENAME) {
        try {
            inputStream = getClass().getResourceAsStream("/Sound/" + FILENAME);
            audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (Exception error) {
            System.out.println("Unable to play audio clip");
        }
    }
}
