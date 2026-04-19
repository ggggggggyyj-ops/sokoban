package sokoban.view;

import sokoban.controller.GameController;
import sokoban.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;

public class LevelSelectFrame extends JFrame {

    private static final Color BG_TOP = new Color(249, 244, 229);
    private static final Color BG_BOTTOM = new Color(236, 244, 228);
    private static final Color CARD = new Color(255, 252, 245, 245);
    private static final Color CARD_BORDER = new Color(214, 206, 184);
    private static final Color TITLE = new Color(98, 73, 46);

    public LevelSelectFrame(GameController controller, int unlockedLevel) {
        setTitle("选关");
        setSize(980, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, BG_TOP,
                        0, getHeight(), BG_BOTTOM
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(196, 225, 184, 70));
                g2.fillOval(-80, 40, 240, 160);

                g2.setColor(new Color(255, 243, 190, 70));
                g2.fillOval(getWidth() - 260, 100, 150, 100);

                g2.setColor(new Color(196, 225, 184, 55));
                g2.fillOval(getWidth() - 220, getHeight() - 170, 190, 130);

                g2.dispose();
            }
        };
        root.setBorder(BorderFactory.createEmptyBorder(18, 24, 24, 24));

        JLabel title = new JLabel("选择关卡", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 34));
        title.setForeground(TITLE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 18, 0));

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);

        JPanel grid = new JPanel(new GridLayout(5, 10, 14, 14));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(26, 26, 26, 26));

        for (int i = 1; i <= 50; i++) {
            boolean selectable = i <= unlockedLevel;
            grid.add(new LevelCard(i, selectable, controller));
        }

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);

                g2.setColor(CARD_BORDER);
                g2.setStroke(new BasicStroke(1.4f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.add(grid, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(840, 438));

        centerWrap.add(card);

        root.add(title, BorderLayout.NORTH);
        root.add(centerWrap, BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }

    private static class LevelCard extends JButton {
        private final int level;
        private final boolean selectable;
        private final GameController controller;

        public LevelCard(int level, boolean selectable, GameController controller) {
            this.level = level;
            this.selectable = selectable;
            this.controller = controller;

            setPreferredSize(new Dimension(68, 68));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(selectable ? new Cursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());

            if (selectable) {
                addActionListener(e -> {
                    SoundPlayer.playClick();
                    controller.loadLevel(level);
                    SwingUtilities.getWindowAncestor(this).dispose();
                });
            } else {
                setEnabled(false);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (selectable) {
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

                g2.setPaint(new GradientPaint(0, 0, c1, 0, h, c2));
                g2.fillRoundRect(0, 0, w, h, 18, 18);

                g2.setColor(new Color(255, 255, 255, 75));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 18, 18);

                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(3, 3, w - 6, h / 2, 14, 14);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("微软雅黑", Font.BOLD, 26));
                String s = String.valueOf(level);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (w - fm.stringWidth(s)) / 2;
                int ty = (h + fm.getAscent()) / 2 - 4;
                g2.drawString(s, tx, ty);

            } else {
                g2.setColor(new Color(231, 228, 222));
                g2.fillRoundRect(0, 0, w, h, 18, 18);

                g2.setColor(new Color(202, 196, 184));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 18, 18);

                drawLock(g2, w, h);

                g2.setColor(new Color(120, 114, 106));
                g2.setFont(new Font("微软雅黑", Font.BOLD, 18));
                String s = String.valueOf(level);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (w - fm.stringWidth(s)) / 2;
                g2.drawString(s, tx, h - 9);
            }

            g2.dispose();
        }

        private void drawLock(Graphics2D g2, int w, int h) {
            int cx = w / 2;
            int bodyW = 26;
            int bodyH = 22;
            int bodyX = cx - bodyW / 2;
            int bodyY = 19;

            g2.setColor(new Color(187, 181, 172));
            g2.fillRoundRect(cx - 10, bodyY + bodyH + 6, 20, 5, 4, 4);

            g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(154, 148, 139));
            g2.drawArc(cx - 10, 9, 20, 18, 0, 180);

            g2.setColor(new Color(197, 192, 184));
            g2.fillRoundRect(bodyX, bodyY, bodyW, bodyH, 8, 8);

            g2.setColor(new Color(151, 145, 136));
            g2.drawRoundRect(bodyX, bodyY, bodyW, bodyH, 8, 8);

            g2.setColor(new Color(129, 124, 116));
            g2.fillOval(cx - 3, bodyY + 6, 6, 6);
            g2.fillRect(cx - 1, bodyY + 11, 2, 6);
        }
    }
}