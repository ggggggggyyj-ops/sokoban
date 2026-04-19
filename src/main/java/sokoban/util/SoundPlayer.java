package sokoban.util;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundPlayer {

    private static Clip moveClip;
    private static Clip pushClip;
    private static Clip winClip;
    private static Clip clickClip;

    static {
        init();
    }

    public static void init() {
        moveClip = loadFromResource("/walk.wav");
        pushClip = loadFromResource("/push.wav");
        winClip = loadFromResource("/win.wav");
        clickClip = loadFromResource("/cotton.wav");
    }

    private static Clip loadFromResource(String resourcePath) {
        try {
            InputStream raw = SoundPlayer.class.getResourceAsStream(resourcePath);
            if (raw == null) {
                return null;
            }

            BufferedInputStream buffered = new BufferedInputStream(raw);
            AudioInputStream audio = AudioSystem.getAudioInputStream(buffered);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (Exception e) {
            return null;
        }
    }

    private static void play(Clip clip) {
        if (clip == null) return;

        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public static void playMove() {
        play(moveClip);
    }

    public static void playPush() {
        play(pushClip);
    }

    public static void playWin() {
        play(winClip);
    }

    public static void playClick() {
        play(clickClip);
    }
}