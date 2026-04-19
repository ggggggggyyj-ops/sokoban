package sokoban.view;

import sokoban.controller.GameController;
import sokoban.model.Direction;
import sokoban.model.GameState;
import sokoban.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

public class GamePanel extends JPanel {

    private static final Color BG_TOP = new Color(249, 244, 229);
    private static final Color BG_BOTTOM = new Color(236, 244, 228);
    private static final Color CARD = new Color(255, 252, 245, 242);
    private static final Color CARD_BORDER = new Color(214, 206, 184);
    private static final Color TITLE = new Color(98, 73, 46);

    private static final Color FLOOR_FILL = new Color(245, 241, 234);
    private static final Color FLOOR_BORDER = new Color(214, 206, 194);

    private final GameController controller;
    private GameState state;

    private JLabel infoLabel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private BoardPanel boardPanel;

    // 从 Maven 资源目录 src/main/resources/sheep.png 读取
    private Image sheepImg;

    public GamePanel(GameState s, GameController controller) {
        this.state = s;
        this.controller = controller;

        setLayout(new BorderLayout());
        setBackground(BG_TOP);
        setFocusable(true);

        buildTopPanel();
        buildBoardPanel();
        buildBottomPanel();
        bindKeyboard();
        loadSheepImage();

        updateInfo();
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private void loadSheepImage() {
        if (sheepImg != null) {
            return;
        }

        try {
            URL url = getClass().getResource("/sheep.png");
            if (url != null) {
                Image img = new ImageIcon(url).getImage();
                if (img.getWidth(null) > 0) {
                    sheepImg = img;
                }
            }
        } catch (Exception e) {
            sheepImg = null;
        }
    }

    private void buildTopPanel() {
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new Dimension(100, 84));

        infoLabel = new JLabel("", SwingConstants.CENTER);
        infoLabel.setFont(new Font("微软雅黑", Font.BOLD, 23));
        infoLabel.setForeground(TITLE);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(12, 28, 12, 28));

        JPanel infoCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);

                g2.setColor(CARD_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);

                g2.dispose();
            }
        };
        infoCard.setOpaque(false);
        infoCard.add(infoLabel, BorderLayout.CENTER);

        topPanel.add(infoCard);
        add(topPanel, BorderLayout.NORTH);
    }

    private void buildBoardPanel() {
        boardPanel = new BoardPanel();
        boardPanel.setOpaque(false);
        add(boardPanel, BorderLayout.CENTER);
    }

    private void buildBottomPanel() {
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(100, 88));

        JPanel buttonCard = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);

                g2.setColor(CARD_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);

                g2.dispose();
            }
        };
        buttonCard.setOpaque(false);

        buttonCard.add(createButton("↑", 64, 42, () -> controller.move(Direction.UP), false));
        buttonCard.add(createButton("↓", 64, 42, () -> controller.move(Direction.DOWN), false));
        buttonCard.add(createButton("←", 64, 42, () -> controller.move(Direction.LEFT), false));
        buttonCard.add(createButton("→", 64, 42, () -> controller.move(Direction.RIGHT), false));
        buttonCard.add(createButton("撤回", 98, 42, controller::undo, true));
        buttonCard.add(createButton("重新开始", 128, 42, controller::restart, true));
        buttonCard.add(createButton("选关", 98, 42, controller::openLevelSelect, true));
        buttonCard.add(createButton("求助", 98, 42, controller::help, true));

        bottomPanel.add(buttonCard);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, int w, int h, Runnable action, boolean playClick) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                ButtonModel model = getModel();
                Color c1;
                Color c2;

                if (model.isPressed()) {
                    c1 = new Color(244, 185, 108);
                    c2 = new Color(225, 152, 80);
                } else if (model.isRollover()) {
                    c1 = new Color(249, 203, 130);
                    c2 = new Color(235, 165, 89);
                } else {
                    c1 = new Color(247, 195, 118);
                    c2 = new Color(229, 159, 84);
                }

                g2.setPaint(new GradientPaint(0, 0, c1, 0, getHeight(), c2));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                g2.setColor(new Color(255, 255, 255, 70));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() / 2, 12, 12);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setPreferredSize(new Dimension(w, h));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("微软雅黑", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            action.run();
            if (playClick) {
                SoundPlayer.playClick();
            }
            requestFocusInWindow();
        });

        return btn;
    }

    private void bindKeyboard() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> controller.move(Direction.UP);
                    case KeyEvent.VK_DOWN -> controller.move(Direction.DOWN);
                    case KeyEvent.VK_LEFT -> controller.move(Direction.LEFT);
                    case KeyEvent.VK_RIGHT -> controller.move(Direction.RIGHT);
                }
            }
        });
    }

    private void updateInfo() {
        infoLabel.setText("第 " + controller.getLevel() + " 关   步数: " + controller.getState().getMoves());
    }

    public void update(GameState s) {
        this.state = s;
        updateInfo();
        boardPanel.repaint();
        revalidate();
        repaint();
        requestFocusInWindow();
    }

    private class BoardPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (state == null || state.getMap() == null) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint bg = new GradientPaint(
                    0, 0, BG_TOP,
                    0, getHeight(), BG_BOTTOM
            );
            g2.setPaint(bg);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(196, 225, 184, 70));
            g2.fillOval(-80, 40, 240, 160);

            g2.setColor(new Color(255, 243, 190, 70));
            g2.fillOval(getWidth() - 320, 70, 140, 90);

            g2.setColor(new Color(196, 225, 184, 55));
            g2.fillOval(getWidth() - 210, getHeight() - 160, 180, 120);

            char[][] map = state.getMap();
            int rows = map.length;
            int cols = map[0].length;

            int marginX = 90;
            int marginY = 12;

            int availableW = Math.max(200, getWidth() - marginX * 2);
            int availableH = Math.max(200, getHeight() - marginY * 2);

            int tile = Math.min(availableW / cols, availableH / rows);
            tile = Math.max(42, Math.min(tile, 58));

            int boardW = cols * tile;
            int boardH = rows * tile;

            int offsetX = (getWidth() - boardW) / 2;
            int offsetY = (getHeight() - boardH) / 2;

            g2.setColor(CARD);
            g2.fillRoundRect(offsetX - 18, offsetY - 18, boardW + 36, boardH + 36, 28, 28);

            g2.setColor(CARD_BORDER);
            g2.drawRoundRect(offsetX - 18, offsetY - 18, boardW + 36, boardH + 36, 28, 28);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    drawTile(g2, map[i][j], offsetX + j * tile, offsetY + i * tile, tile);
                }
            }

            g2.dispose();
        }

        private void drawTile(Graphics2D g2, char tileType, int x, int y, int tile) {
            int arc = Math.max(12, tile / 4);

            if (tileType == '1') {
                Shape wall = new RoundRectangle2D.Float(x + 2, y + 2, tile - 4, tile - 4, arc, arc);
                g2.setColor(new Color(133, 208, 136));
                g2.fill(wall);

                g2.setColor(new Color(255, 255, 255, 18));
                g2.draw(new RoundRectangle2D.Float(x + 6, y + 6, tile - 12, tile - 12, arc - 4, arc - 4));
                return;
            }

            Shape floor = new RoundRectangle2D.Float(x + 2, y + 2, tile - 4, tile - 4, arc, arc);
            g2.setColor(FLOOR_FILL);
            g2.fill(floor);

            g2.setColor(FLOOR_BORDER);
            g2.setStroke(new BasicStroke(1.6f));
            g2.draw(new RoundRectangle2D.Float(x + 2, y + 2, tile - 4, tile - 4, arc, arc));

            if (tileType == '2' || tileType == '5' || tileType == '6') {
                drawFlowerTarget(g2, x, y, tile);
            }

            if (tileType == '3' || tileType == '5') {
                drawCuteBox(g2, x, y, tile);
            }

            if (tileType == '4' || tileType == '6') {
                drawLazyYangYang(g2, x, y, tile);
            }
        }

        private void drawFlowerTarget(Graphics2D g2, int x, int y, int tile) {
            int cx = x + tile / 2;
            int cy = y + tile / 2;
            int petal = tile / 4;

            g2.setColor(new Color(255, 221, 118));
            g2.fillOval(cx - petal / 2, cy - tile / 3, petal, petal);
            g2.fillOval(cx - petal / 2, cy + tile / 3 - petal, petal, petal);
            g2.fillOval(cx - tile / 3, cy - petal / 2, petal, petal);
            g2.fillOval(cx + tile / 3 - petal, cy - petal / 2, petal, petal);

            g2.setColor(new Color(255, 236, 160));
            g2.fillOval(cx - petal / 2 - tile / 7, cy - petal / 2 - tile / 7, petal, petal);
            g2.fillOval(cx - petal / 2 + tile / 7, cy - petal / 2 - tile / 7, petal, petal);
            g2.fillOval(cx - petal / 2 - tile / 7, cy - petal / 2 + tile / 7, petal, petal);
            g2.fillOval(cx - petal / 2 + tile / 7, cy - petal / 2 + tile / 7, petal, petal);

            g2.setColor(new Color(249, 183, 90));
            g2.fillOval(cx - petal / 2, cy - petal / 2, petal, petal);
        }

        private void drawCuteBox(Graphics2D g2, int x, int y, int tile) {
            int size = (int) (tile * 0.72);
            int bx = x + (tile - size) / 2;
            int by = y + (tile - size) / 2;

            GradientPaint gp = new GradientPaint(
                    bx, by, new Color(220, 173, 114),
                    bx, by + size, new Color(195, 141, 85)
            );
            g2.setPaint(gp);
            g2.fillRoundRect(bx, by, size, size, 14, 14);

            g2.setColor(new Color(129, 85, 48));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(bx, by, size, size, 14, 14);
            g2.drawLine(bx + 8, by + 8, bx + size - 8, by + size - 8);
            g2.drawLine(bx + size - 8, by + 8, bx + 8, by + size - 8);

            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawLine(bx + 10, by + 10, bx + size - 10, by + 10);
        }

        private void drawLazyYangYang(Graphics2D g2, int x, int y, int tile) {
            loadSheepImage();

            if (sheepImg == null || sheepImg.getWidth(null) <= 0) {
                return;
            }

            int size = (int) (tile * 0.82);
            int px = x + (tile - size) / 2;
            int py = y + (tile - size) / 2;

            g2.drawImage(sheepImg, px, py, size, size, null);
        }
    }
}