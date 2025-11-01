package ui.scene;

import core.InputHandler;
import core.SceneManager;
import game.LevelData; // import
import game.LevelManager; // import
import ui.base.Button;
import ui.base.Scene;
import ui.button.LeftArrowButton;
import ui.button.PlayButton;
import ui.button.RightArrowButton;
import ui.element.Label;
import utils.Constants;

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

    private static final int PREVIEW_WIDTH = 800;
    private static final int PREVIEW_HEIGHT = 450;
    private static final int PREVIEW_X = (Constants.WINDOW_WIDTH - PREVIEW_WIDTH) / 2; // = 240
    private static final int PREVIEW_Y = 120;


    public LevelSelectScene(InputHandler input, SceneManager sceneManager) {
        super("LevelSelectScene", input);
        this.sceneManager = sceneManager;

        initUI();
    }

    @Override
    protected void initUI() {
        // --- Quản lý dữ liệu Level ---
        levelManager = new LevelManager();
        levelManager.loadAllLevels("assets/levels"); // Quét thư mục
        availableLevels = levelManager.getAllLevels();

        // --- Khởi tạo UI ---
        Font titleFont = new Font("Arial", Font.BOLD, 48);

        // [SỬA LỖI] Tính toán x căn giữa TRƯỚC KHI gọi constructor
        FontMetrics fmTitle = new Canvas().getFontMetrics(titleFont);
        int titleWidth = fmTitle.stringWidth("SELECT LEVEL");
        int centeredX = (Constants.WINDOW_WIDTH - titleWidth) / 2;

        // Tạo Label với x đã được căn giữa
        titleLabel = new Label("SELECT LEVEL", centeredX, 40, titleFont, Color.WHITE);

        // Dòng lỗi `titleLabel.x = ...` đã được xóa.

        // --- Khởi tạo các nút ---
        // Nút mũi tên trái
        buttons.add(new LeftArrowButton(PREVIEW_X - 80, PREVIEW_Y + PREVIEW_HEIGHT / 2 - 30, 60, 60));
        buttons.get(0).setActivity(this::selectPreviousLevel);

        // Nút mũi tên phải
        buttons.add(new RightArrowButton(PREVIEW_X + PREVIEW_WIDTH + 20, PREVIEW_Y + PREVIEW_HEIGHT / 2 - 30, 60, 60));
        buttons.get(1).setActivity(this::selectNextLevel);

        // Nút Play
        buttons.add(new PlayButton("Play", Constants.WINDOW_WIDTH / 2 - 60, PREVIEW_Y + PREVIEW_HEIGHT + 60, 120, 50, new Font("Serif", Font.BOLD, 32), this::playSelectedLevel));
    }

    // Các phương thức xử lý logic chọn level
    private void selectNextLevel() {
        if (availableLevels == null || availableLevels.isEmpty()) return;
        currentLevelIndex = (currentLevelIndex + 1) % availableLevels.size();
    }

    private void selectPreviousLevel() {
        if (availableLevels == null || availableLevels.isEmpty()) return;
        currentLevelIndex = (currentLevelIndex - 1 + availableLevels.size()) % availableLevels.size();
    }

    private void playSelectedLevel() {
        if (availableLevels != null && !availableLevels.isEmpty()) {
            String selectedPath = availableLevels.get(currentLevelIndex).filePath;
            sceneManager.goToGame(selectedPath); // GỌI PHIÊN BẢN MỚI
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
                button.performAction(); // Sử dụng performAction()
            }
        }
    }

    @Override
    protected void render(Graphics2D g) {
        // Vẽ tiêu đề "SELECT LEVEL"
        titleLabel.draw(g);

        // Vẽ khung preview
        g.setColor(Color.WHITE);
        g.drawRect(PREVIEW_X, PREVIEW_Y, PREVIEW_WIDTH, PREVIEW_HEIGHT);

        // Vẽ thông tin level được chọn
        if (availableLevels != null && !availableLevels.isEmpty()) {
            LevelData selectedLevel = availableLevels.get(currentLevelIndex);

            // Vẽ ảnh preview
            if (selectedLevel.previewImage != null) {
                g.drawImage(selectedLevel.previewImage, PREVIEW_X + 1, PREVIEW_Y + 1, PREVIEW_WIDTH - 2, PREVIEW_HEIGHT - 2, null);
            } else {
                // Hiển thị thông báo nếu không tải được ảnh
                g.setColor(Color.DARK_GRAY);
                g.fillRect(PREVIEW_X + 1, PREVIEW_Y + 1, PREVIEW_WIDTH - 2, PREVIEW_HEIGHT - 2);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Serif", Font.ITALIC, 20));
                String noPreviewMsg = "No Preview Image";
                FontMetrics fmMsg = g.getFontMetrics();
                int msgWidth = fmMsg.stringWidth(noPreviewMsg);
                g.drawString(noPreviewMsg, (Constants.WINDOW_WIDTH - msgWidth) / 2, PREVIEW_Y + PREVIEW_HEIGHT / 2);
            }

            // Vẽ tên level bên dưới
            g.setFont(new Font("Serif", Font.BOLD, 32));
            FontMetrics fm = g.getFontMetrics();
            String levelName = selectedLevel.name.toUpperCase();
            int textWidth = fm.stringWidth(levelName);
            g.setColor(Color.WHITE);
            g.drawString(levelName, (Constants.WINDOW_WIDTH - textWidth) / 2, PREVIEW_Y + PREVIEW_HEIGHT + 45);

        } else {
            // Thông báo nếu không tìm thấy level
            g.setFont(new Font("Serif", Font.ITALIC, 24));
            String message = "No levels found in 'assets/levels'";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(message);
            g.setColor(Color.GRAY);
            g.drawString(message, (Constants.WINDOW_WIDTH - textWidth) / 2, PREVIEW_Y + PREVIEW_HEIGHT / 2);
        }

        // Vẽ các nút
        for (Button button : buttons) {
            button.draw(g);
        }
    }
}
