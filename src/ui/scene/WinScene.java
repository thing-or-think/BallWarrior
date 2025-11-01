package ui.scene;

import core.InputHandler;
import core.ResourceLoader;
import ui.base.Button;
import ui.base.Scene;
import ui.button.MenuButton;
import utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class WinScene extends Scene {

    private final Runnable onNextLevel; // Hành động cho nút Next
    private final Runnable onMainMenu;  // Hành động cho nút Menu
    private final List<Button> buttons = new ArrayList<>();
    private BufferedImage backgroundImage;

    public WinScene(InputHandler input, Runnable onNextLevel, Runnable onMainMenu) {
        super("WinScene", input);
        this.onNextLevel = onNextLevel;
        this.onMainMenu = onMainMenu;
        initUI();
    }

    @Override
    protected void initUI() {
        setBackground(Color.BLACK);
        // Tải ảnh nền WinGame.png
        backgroundImage = ResourceLoader.loadImage("assets/images/WinGame.png");

        Font font = new Font("Serif", Font.PLAIN, 32);
        FontMetrics fm = getFontMetrics(font);

        // Nút "NEXT LEVEL"
        buttons.add(new MenuButton("NEXT LEVEL",
                Constants.WINDOW_WIDTH / 2,
                Constants.WINDOW_HEIGHT / 2 + 100,
                fm,
                onNextLevel // Gán hành động
        ));

        // Nút "MAIN MENU"
        buttons.add(new MenuButton("MAIN MENU",
                Constants.WINDOW_WIDTH / 2,
                Constants.WINDOW_HEIGHT / 2 + 160, // Dịch xuống một chút
                fm,
                onMainMenu // Gán hành động
        ));
    }

    @Override
    protected void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();

        // Cập nhật trạng thái hover/click cho cả hai nút
        for (Button button : buttons) {
            button.setHovered(button.contains(mx, my));
            if (button.isHovered() && input.consumeClick()) {
                button.onClick();
            }
        }
    }

    @Override
    protected void render(Graphics2D g2) {
        // Vẽ ảnh nền
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, this);
        } else {
            // Fallback nếu ảnh lỗi
            g2.setColor(Color.GREEN);
            g2.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Serif", Font.BOLD, 50));
            String winText = "YOU WIN!";
            int textWidth = g2.getFontMetrics().stringWidth(winText);
            g2.drawString(winText, (Constants.WINDOW_WIDTH - textWidth) / 2, Constants.WINDOW_HEIGHT / 2 - 50);
        }

        // Vẽ các nút
        for (Button button : buttons) {
            button.draw(g2);
        }
    }
}
