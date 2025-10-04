package ui;

import core.InputHandler;
import core.ResourceLoader;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GameOverScene extends JPanel {
    private final InputHandler input;
    private final Runnable onMainMenu;
    private final List<Button> buttons = new ArrayList<>();
    private BufferedImage backgroundImage;

    // Constructor
    public GameOverScene(InputHandler input, Runnable onMainMenu) {
        this.input = input;
        this.onMainMenu = onMainMenu;

        initUI();
        initButtons();
        initMouse();
        startRepaintTimer();
    }

    /** Khởi tạo UI cơ bản */
    private void initUI() {
        setBackground(Color.BLACK);
        // Tải hình nền gameover.png
        backgroundImage = ResourceLoader.loadImg("assets/images/gameover.png");
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT)); // Đảm bảo kích thước khớp với frame
    }

    /** Tạo các button */
    private void initButtons() {
        Font font = new Font("Serif", Font.PLAIN, 32);
        FontMetrics fm = getFontMetrics(font);

        // Nút "Main Menu"
        buttons.add(new Button("MAIN MENU", Constants.WIDTH / 2, Constants.HEIGHT / 2 + 100, fm));
    }

    /** Gắn mouse input */
    private void initMouse() {
        addMouseListener(input.createMouseAdapter());
        addMouseMotionListener(input.createMouseAdapter());
    }

    /** Khởi động timer repaint */
    private void startRepaintTimer() {
        new javax.swing.Timer(16, e -> repaint()).start();
    }

    /** Cập nhật trạng thái menu */
    private void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();

        for (Button button : buttons) {
            button.hovered = button.contains(mx, my);
            if (button.hovered && input.consumeClick()) {
                handleButtonClick(button.text);
            }
        }
    }

    /** Xử lý sự kiện click button */
    private void handleButtonClick(String text) {
        System.out.println("Clicked " + text);
        if (text.equals("MAIN MENU")) {
            input.resetMouse(); // Reset chuột để tránh click đúp
            onMainMenu.run();
        }
    }

    /** Vẽ background và nút */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        update();

        // Vẽ hình nền
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, Constants.WIDTH, Constants.HEIGHT, this);
        } else {
            // Fallback nếu không tải được ảnh
            g2.setColor(Color.RED);
            g2.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Serif", Font.BOLD, 50));
            String gameOverText = "GAME OVER";
            int textWidth = g2.getFontMetrics().stringWidth(gameOverText);
            g2.drawString(gameOverText, (Constants.WIDTH - textWidth) / 2, Constants.HEIGHT / 2 - 50);
        }

        // Vẽ các nút
        for (Button button : buttons) {
            button.draw(g2);
        }
    }
}
