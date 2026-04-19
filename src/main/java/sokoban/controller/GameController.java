package sokoban.controller;

import sokoban.model.Direction;
import sokoban.model.GameState;
import sokoban.model.LevelGenerator;
import sokoban.model.SokobanSolver;
import sokoban.util.SaveManager; // ✅ 新增
import sokoban.util.SoundPlayer;
import sokoban.view.GamePanel;
import sokoban.view.LevelSelectFrame;

import javax.swing.*;
import java.util.List;

public class GameController {

    private GameState state = new GameState();
    private GamePanel panel;

    private int level = 1;
    private int unlockedLevel = 1;

    // ===== 自动演示 =====
    private Timer demoTimer;
    private boolean helping = false;

    public GameController() {
        SoundPlayer.init();

        // ✅ 读取存档
        unlockedLevel = SaveManager.load();

        loadLevel(1);
        panel = new GamePanel(state, this);
    }

    // ===== 加载关卡 =====
    public void loadLevel(int l) {
        stopHelpDemo();

        level = l;
        state.loadLevel(LevelGenerator.generateLevel(l));

        if (panel != null) {
            panel.update(state);
        }
    }

    // ===== 移动 =====
    public void move(Direction d) {
        if (helping) return;
        doMove(d);
    }

    // ===== 内部移动 =====
    private void doMove(Direction d) {
        if (state.move(d)) {
            SoundPlayer.playMove();

            if (panel != null) {
                panel.update(state);
            }

            if (checkWin()) {
                SoundPlayer.playWin();
                stopHelpDemo();
                JOptionPane.showMessageDialog(panel, "恭喜通关！");
                win();
            }
        }
    }

    // ===== 撤回 =====
    public void undo() {
        if (helping) return;

        state.undo();
        if (panel != null) {
            panel.update(state);
        }
    }

    // ===== 重新开始 =====
    public void restart() {
        stopHelpDemo();
        loadLevel(level);
    }

    // ===== 通关处理 =====
    public void win() {
        stopHelpDemo();

        if (level == unlockedLevel) {
            unlockedLevel++;

            // ✅ 自动保存进度
            SaveManager.save(unlockedLevel);
        }

        if (level < 50) {
            loadLevel(level + 1);
        } else {
            JOptionPane.showMessageDialog(panel, "你已经通关全部关卡！");
        }
    }

    // ===== 选关 =====
    public void openLevelSelect() {
        stopHelpDemo();
        new LevelSelectFrame(this, unlockedLevel);
    }

    // ===== 求助：自动演示 =====
    public void help() {
        if (helping) return;

        List<Direction> path = SokobanSolver.solvePath(state.getMap());

        if (path == null || path.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "当前状态无解或已通关！");
            return;
        }

        helping = true;
        final int[] idx = {0};

        demoTimer = new Timer(220, e -> {
            if (idx[0] >= path.size()) {
                stopHelpDemo();
                return;
            }

            Direction next = path.get(idx[0]);
            idx[0]++;

            doMove(next);

            if (!helping) {
                ((Timer) e.getSource()).stop();
            }
        });

        demoTimer.start();
    }

    private void stopHelpDemo() {
        helping = false;
        if (demoTimer != null) {
            demoTimer.stop();
            demoTimer = null;
        }
    }

    // ===== 胜利判断 =====
    private boolean checkWin() {
        char[][] map = state.getMap();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == '2' || map[i][j] == '6') {
                    return false;
                }
            }
        }
        return true;
    }

    // ===== Getter =====
    public GamePanel getPanel() {
        return panel;
    }

    public GameState getState() {
        return state;
    }

    public int getLevel() {
        return level;
    }

    public int getUnlockedLevel() {
        return unlockedLevel;
    }
}