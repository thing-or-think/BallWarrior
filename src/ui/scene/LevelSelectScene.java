package ui.scene;

import core.InputHandler;
import core.SceneManager;
import ui.base.Button;
import ui.base.Scene;
import ui.button.LeftArrowButton;
import ui.button.PlayButton;
import ui.button.RightArrowButton;
import ui.element.Label;
import utils.Constants;
import game.LevelManager;
import game.LevelData;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

public class LevelSelectScene extends Scene {

    private final SceneManager sceneManager;
    private final List<Button> buttons = new ArrayList<>();
    private Label titleLabel;

    // Các biến để quản lý việc chọn level
    private LevelManager levelManager;
    private List<LevelData> availableLevels;
    private int currentLevelIndex = 0;

    // Vị trí và kích thước khung preview
    private static final int PREVIEW_WIDTH = 500;
    private static final int PREVIEW_HEIGHT = 360;

    public LevelSelectScene(InputHandler input, SceneManager sceneManager) {
        super("LevelSelectScene", input);
        this.sceneManager = sceneManager;

        initUI();
    }

    @Override
    protected void initUI() {

        levelManager = new LevelManager();
        levelManager.loadAllLevels("assets/levels");
        availableLevels = levelManager.getAllLevels();

        // Title
        Font titleFont = new Font("Arial", Font.BOLD, 48);
        titleLabel = new Label(
                "SELECT LEVEL",
                Constants.WINDOW_WIDTH / 2 - 200,
                60,
                titleFont,
                Color.WHITE
        );

        int boxW = PREVIEW_WIDTH;
        int boxH = PREVIEW_HEIGHT;

        int boxX = (Constants.WINDOW_WIDTH - boxW) / 2;
        int boxY = 140;

        // ---- LEFT BUTTON ----
        Button left = new LeftArrowButton(
                boxX - 80,
                boxY + boxH / 2 - 30,
                60,
                60
        );
        left.setActivity(this::selectPreviousLevel);
        buttons.add(left);

        // ---- RIGHT BUTTON ----
        Button right = new RightArrowButton(
                boxX + boxW + 20,
                boxY + boxH / 2 - 30,
                60,
                60
        );
        right.setActivity(this::selectNextLevel);
        buttons.add(right);

        // ---- PLAY BUTTON ----
        Font playFont = new Font("Serif", Font.BOLD, 32);
        int playW = 160;
        int playH = 60;

        Button play = new PlayButton(
                "Play",
                Constants.WINDOW_WIDTH / 2 - playW / 2,
                boxY + boxH + 80,
                playW,
                playH,
                playFont,
                this::playSelectedLevel
        );
        buttons.add(play);
    }

    @Override
    protected void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();
        for (Button button : buttons) {
            button.setHovered(button.contains(mx, my));
            if (button.isHovered() && input.consumeClick()) {
                button.onClick();
            }
        }
    }

    // Các phương thức xử lý logic chọn level
    private void selectNextLevel() {
        if (availableLevels.isEmpty()) return;
        currentLevelIndex = (currentLevelIndex + 1) % availableLevels.size();
    }

    private void selectPreviousLevel() {
        if (availableLevels.isEmpty()) return;
        currentLevelIndex = (currentLevelIndex - 1 + availableLevels.size()) % availableLevels.size();
    }

    private void playSelectedLevel() {
        if (!availableLevels.isEmpty()) {
            String selectedPath = availableLevels.get(currentLevelIndex).filePath;
            sceneManager.goToGame(selectedPath);
        } else {
            System.out.println("Không có level nào để chơi!");
        }
    }

    @Override
    protected void render(Graphics2D g) {

        titleLabel.draw(g);

        int boxW = PREVIEW_WIDTH;
        int boxH = PREVIEW_HEIGHT;

        int boxX = (Constants.WINDOW_WIDTH - boxW) / 2;
        int boxY = 140;

        g.setColor(Color.WHITE);
        g.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);

        if (!availableLevels.isEmpty()) {

            LevelData selected = availableLevels.get(currentLevelIndex);

            if (selected.previewImage != null) {
                g.drawImage(selected.previewImage, boxX, boxY, boxW, boxH, null);
            } else {
                g.setColor(new Color(80, 80, 80));
                g.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Serif", Font.ITALIC, 24));
                g.drawString("No Preview Image", boxX + 100, boxY + boxH / 2);
            }

            g.setFont(new Font("Serif", Font.BOLD, 32));
            g.setColor(Color.WHITE);

            String name = selected.name.toUpperCase();
            int nameW = g.getFontMetrics().stringWidth(name);
            g.drawString(name,
                    (Constants.WINDOW_WIDTH - nameW) / 2,
                    boxY + boxH + 40
            );
        }

        // Draw buttons
        for (Button b : buttons) {
            b.draw(g);
        }
    }

}
