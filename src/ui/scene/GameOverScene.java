package ui.scene;

import core.InputHandler;
import core.ResourceLoader;
import ui.base.Scene;
import ui.base.Button;
import ui.button.MenuButton;
import utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameOverScene extends Scene {
    private final Runnable onMainMenu;
    private Button button;
    private BufferedImage backgroundImage;

    public GameOverScene(InputHandler input, Runnable onMainMenu) {
        super("GameOverScene", input);
        this.onMainMenu = onMainMenu;

        initUI();
    }

    @Override
    protected void initUI() {
        setBackground(Color.BLACK);
        backgroundImage = ResourceLoader.loadImage("assets/images/gameover.png");

        Font font = new Font("Serif", Font.PLAIN, 32);
        FontMetrics fm = getFontMetrics(font);

        // Nút "MAIN MENU"
        button = new MenuButton("MAIN MENU",
                Constants.WINDOW_WIDTH / 2,
                Constants.WINDOW_HEIGHT / 2 + 100,
                fm);

        button.setActivity(onMainMenu);
    }

    @Override
    protected void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();

        button.setHovered(button.contains(mx, my));
        if (button.isHovered() && input.consumeClick()) {
            button.onClick();
        }
    }

    @Override
    protected void render(Graphics2D g2) {
        // Nếu có background ảnh riêng, vẽ đè lên
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, this);
        } else {
            g2.setColor(Color.RED);
            g2.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Serif", Font.BOLD, 50));
            String gameOverText = "GAME OVER";
            int textWidth = g2.getFontMetrics().stringWidth(gameOverText);
            g2.drawString(gameOverText, (Constants.WINDOW_WIDTH - textWidth) / 2, Constants.WINDOW_HEIGHT / 2 - 50);
        }

        // Vẽ các nút
        button.draw(g2);
    }
}
