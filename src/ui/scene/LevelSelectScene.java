package ui.scene;

import core.InputHandler;
import game.LevelData;
import core.SceneManager;
import ui.base.Button;
import ui.base.Scene;
import ui.button.LeftArrowButton;
import ui.button.PlayButton;
import ui.button.RightArrowButton;
import ui.element.Label;
import utils.Constants;
import game.LevelManager;


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
    private static final int PREVIEW_X = 150;
    private static final int PREVIEW_Y = 120;
    private static final int PREVIEW_WIDTH = 500;
    private static final int PREVIEW_HEIGHT = 360;

    public LevelSelectScene(InputHandler input, SceneManager sceneManager) {
        super("LevelSelectScene", input);
        this.sceneManager = sceneManager;

        initUI();
    }

    @Override
    protected void initUI() {
        // --- Quản lý dữ liệu Level ---
        levelManager = new LevelManager();
        levelManager.loadAllLevels("assets/levels");
        availableLevels = levelManager.getAllLevels();

        // --- Khởi tạo UI ---
        Font titleFont = new Font("Arial", Font.BOLD, 48);
        titleLabel = new Label("SELECT LEVEL", 0, 40, titleFont, Color.WHITE);
        // Căn giữa tiêu đề
        FontMetrics fmTitle = new Canvas().getFontMetrics(titleFont);
        titleLabel = new Label("SELECT LEVEL", (Constants.WIDTH - fmTitle.stringWidth("SELECT LEVEL")) / 2, 40, titleFont, Color.WHITE);


        // --- Khởi tạo các nút ---
        // Nút mũi tên trái
        buttons.add(new LeftArrowButton(PREVIEW_X - 80, PREVIEW_Y + PREVIEW_HEIGHT / 2 - 30, 60, 60));
        buttons.get(0).setActivity(this::selectPreviousLevel);

        // Nút mũi tên phải
        buttons.add(new RightArrowButton(PREVIEW_X + PREVIEW_WIDTH + 20, PREVIEW_Y + PREVIEW_HEIGHT / 2 - 30, 60, 60));
        buttons.get(1).setActivity(this::selectNextLevel);

        // Nút Play
        buttons.add(new PlayButton("Play", Constants.WIDTH / 2 - 60, PREVIEW_Y + PREVIEW_HEIGHT + 40, 120, 50, new Font("Serif", Font.BOLD, 32), this::playSelectedLevel)); // Y từ 80 -> 15 để lên cao hơn
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
    protected void update() {
        // Quay về Menu bằng phím ESC
        if (input.isKeyJustPressed(KeyEvent.VK_ESCAPE)) {
            sceneManager.goToMenu();
        }

        int mx = input.getMouseX();
        int my = input.getMouseY();
        for (Button button : buttons) {
            button.setHovered(button.contains(mx, my));
            if (button.isHovered() && input.consumeClick()) {
                button.performAction(); // Sử dụng performAction() để gọi Runnable
            }
        }
    }

    @Override
    protected void render(Graphics2D g) {
        // Vẽ tiêu đề "SELECT LEVEL"
        titleLabel.draw(g);

        // Vẽ khung preview
        g.setColor(Color.BLACK);
        g.drawRect(150, 120, 500, 360);

        // Vẽ thông tin level được chọn
        if (availableLevels != null && !availableLevels.isEmpty()) {
            LevelData selectedLevel = availableLevels.get(currentLevelIndex);

            if (selectedLevel.previewImage != null) {
                g.drawImage(selectedLevel.previewImage, PREVIEW_X, PREVIEW_Y, PREVIEW_WIDTH, PREVIEW_HEIGHT, null);
            } else {
                // Hiển thị thông báo nếu không tải được ảnh
                g.setColor(Color.DARK_GRAY);
                g.fillRect(PREVIEW_X + 1, PREVIEW_Y + 1, PREVIEW_WIDTH - 2, PREVIEW_HEIGHT - 2);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Serif", Font.ITALIC, 20));
                String noPreviewMsg = "No Preview Image";
                FontMetrics fmMsg = g.getFontMetrics();
                int msgWidth = fmMsg.stringWidth(noPreviewMsg);
                g.drawString(noPreviewMsg, (Constants.WIDTH - msgWidth) / 2, PREVIEW_Y + PREVIEW_HEIGHT / 2);
            }

            // Vẽ tên level
            g.setFont(new Font("Serif", Font.BOLD, 32));
            FontMetrics fm = g.getFontMetrics();
            String levelName = selectedLevel.name.toUpperCase();
            int textWidth = fm.stringWidth(levelName);
            g.setColor(Color.WHITE);
            g.drawString(levelName, (Constants.WIDTH - textWidth) / 2, PREVIEW_Y + PREVIEW_HEIGHT + 30); // Y từ 50 -> 25

        } else {
            // Thông báo nếu không tìm thấy level
            g.setFont(new Font("Serif", Font.ITALIC, 24));
            String message = "No levels found in 'assets/levels'";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(message);
            g.setColor(Color.GRAY);
            g.drawString(message, (Constants.WIDTH - textWidth) / 2, PREVIEW_Y + PREVIEW_HEIGHT / 2);
        }
        // Vẽ các nút
        for (Button button : buttons) {
            button.draw(g);
        }
    }
}
