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

public class LevelSelectScene extends Scene {

    private final SceneManager sceneManager;
    private final List<Button> buttons = new ArrayList<>();
    private Label titleLabel;

    // Các biến để quản lý việc chọn level
    private LevelManager levelManager;
    private List<LevelData> availableLevels;
    private int currentLevelIndex = 0;

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
        buttons.add(new LeftArrowButton(40, 300, 60, 60));
        buttons.get(0).setActivity(this::selectPreviousLevel);

        // Nút mũi tên phải
        buttons.add(new RightArrowButton(Constants.WIDTH - 100, 300, 60, 60));
        buttons.get(1).setActivity(this::selectNextLevel);

        // Nút Play
        buttons.add(new PlayButton("Play", Constants.WIDTH / 2 - 60, 520, 120, 40, new Font("Serif", Font.PLAIN, 32), this::playSelectedLevel));
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
        g.setColor(Color.WHITE);
        g.drawRect(150, 120, 500, 360);

        // Vẽ thông tin level được chọn
        if (availableLevels != null && !availableLevels.isEmpty()) {
            LevelData selectedLevel = availableLevels.get(currentLevelIndex);

            // Vẽ tên level
            g.setFont(new Font("Serif", Font.BOLD, 40));
            FontMetrics fm = g.getFontMetrics();
            String levelName = selectedLevel.name.toUpperCase();
            int textWidth = fm.stringWidth(levelName);
            g.drawString(levelName, (Constants.WIDTH - textWidth) / 2, 320);

        } else {
            // Thông báo nếu không tìm thấy level
            g.setFont(new Font("Serif", Font.ITALIC, 24));
            FontMetrics fm = g.getFontMetrics();
            String message = "No levels found in 'assets/levels'";
            int textWidth = fm.stringWidth(message);
            g.setColor(Color.GRAY);
            g.drawString(message, (Constants.WIDTH - textWidth) / 2, 320);
        }


        // Vẽ các nút
        for (Button button : buttons) {
            button.draw(g);
        }
    }
}
