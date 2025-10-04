    package ui;

    import utils.Constants;
    import core.InputHandler;

    import javax.swing.*;
    import java.awt.*;
    import java.util.ArrayList;
    import java.util.List;

    public class ShopScene extends JPanel {
        private final List<Button> buttons = new ArrayList<>();
        private final InputHandler input;
        private final Runnable onBack;

        private boolean mouseHandled = false;

        public ShopScene(InputHandler input, Runnable onBack) {
            this.input = input;
            this.onBack = onBack;

            setBackground(Color.DARK_GRAY);

            Font font = new Font("Serif", Font.PLAIN, 28);
            FontMetrics fm = getFontMetrics(font);

            // Danh sách vật phẩm demo
            String[] items = {"Speed +5", "Bigger Paddle", "Extra Life"};
            int startY = 200;
            int spacing = 50;

            for (int i = 0; i < items.length; i++) {
                buttons.add(new Button(items[i], Constants.WIDTH / 2, startY + i * spacing, fm));
            }
            // Nút back
            buttons.add(new Button("BACK", Constants.WIDTH / 2, startY + items.length * spacing + 50, fm));

            addMouseListener(input.createMouseAdapter());
            addMouseMotionListener(input.createMouseAdapter());

            new javax.swing.Timer(16, e -> repaint()).start();
        }

        private void update() {
            int mx = input.getMouseX();
            int my = input.getMouseY();

            for (Button button : buttons) {
                button.hovered = button.contains(mx, my);

                if (button.hovered && input.isMousePressed() && !mouseHandled) {
                    mouseHandled = true;
                    System.out.println("Clicked " + button.text);
                    if (button.text.equals("BACK")) {
                        input.resetMouse();
                        onBack.run();
                    } else {
                        System.out.println("Mua: " + button.text);
                    }
                }
            }

            if (!input.isMousePressed()) {
                mouseHandled = false;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("Serif", Font.PLAIN, 28));
            g2.setColor(Color.WHITE);

            g2.drawString("SHOP", Constants.WIDTH / 2 - 40, 150);

            update();

            for (Button button : buttons) {
                button.draw(g2);
            }
        }
    }
