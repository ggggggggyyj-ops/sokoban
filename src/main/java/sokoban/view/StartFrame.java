package sokoban.view;

import sokoban.controller.GameController;
import sokoban.util.SoundPlayer;

import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {

    private static final Color BG_TOP = new Color(249, 244, 229);
    private static final Color BG_BOTTOM = new Color(236, 244, 228);
    private static final Color CARD = new Color(255, 252, 245, 245);
    private static final Color CARD_BORDER = new Color(214, 206, 184);
    private static final Color TITLE = new Color(98, 73, 46);

    public StartFrame() {
        setTitle("游戏");
        setSize(980, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel() {
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
                g2.fillOval(-90, 50, 260, 180);

                g2.setColor(new Color(255, 243, 190, 70));
                g2.fillOval(getWidth() - 250, 90, 150, 100);

                g2.setColor(new Color(196, 225, 184, 55));
                g2.fillOval(getWidth() - 220, getHeight() - 180, 190, 130);

                g2.setColor(CARD);
                g2.fillRoundRect(205, 115, 570, 390, 38, 38);

                g2.setColor(CARD_BORDER);
                g2.drawRoundRect(205, 115, 570, 390, 38, 38);

                g2.dispose();
            }
        };
        root.setLayout(null);

        JLabel title = new JLabel("推箱子 Sokoban", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 42));
        title.setForeground(TITLE);
        title.setBounds(240, 160, 500, 64);

        JButton start = createMainButton("开始游戏");
        start.setBounds(330, 280, 320, 64);

        JButton exit = createMainButton("退出游戏");
        exit.setBounds(330, 375, 320, 64);

        start.addActionListener(e -> {
            SoundPlayer.playClick();

            GameController controller = new GameController();

            JFrame game = new JFrame("游戏");
            game.setSize(980, 680);
            game.setLocationRelativeTo(null);
            game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            game.setLayout(new BorderLayout());
            game.add(controller.getPanel(), BorderLayout.CENTER);
            game.setVisible(true);

            SwingUtilities.invokeLater(() -> controller.getPanel().requestFocusInWindow());
            dispose();
        });

        exit.addActionListener(e -> {
            SoundPlayer.playClick();
            System.exit(0);
        });

        root.add(title);
        root.add(start);
        root.add(exit);

        add(root);
        setVisible(true);
    }

    private JButton createMainButton(String text) {
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);

                g2.setColor(new Color(255, 255, 255, 75));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);

                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() / 2, 18, 18);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("微软雅黑", Font.BOLD, 24));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }
}