package sokoban;

import sokoban.util.SoundPlayer;
import sokoban.view.StartFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SoundPlayer.init();
            new StartFrame();
        });
    }
}