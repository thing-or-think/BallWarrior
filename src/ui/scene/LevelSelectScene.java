package ui.scene;

import core.InputHandler;
import core.SceneManager;
import ui.base.Scene;
import ui.button.LevelButton;
import utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class LevelSelectScene extends Scene {

    private final SceneManager sceneManager;
    private final List<LevelButton> levelButtons = new ArrayList<>();
    private int selectedIndex = 0;
    private Font titleFont;

    public LevelSelectScene(InputHandler input, SceneManager sceneManager) {
        super("LevelSelectScene", input);
        this.sceneManager = sceneManager;
        initUI();
    }

    @Override
    protected void initUI() {
        titleFont = new Font("Arial", Font.BOLD, 48);
        FontMetrics fm = getFontMetrics(new Font("Serif", Font.PLAIN, 32));

        // Tạo các nút màn chơi
        levelButtons.add(new LevelButton("Level 1", "Dễ", null, Constants.WIDTH / 2, 200, fm));
        levelButtons.add(new LevelButton("Level 2", "Trung bình", null, Constants.WIDTH / 2, 320, fm));
        levelButtons.add(new LevelButton("Level 3", "Khó", null, Constants.WIDTH / 2, 440, fm));
        levelButtons.add(new LevelButton("Level 4", "Siêu khó", null, Constants.WIDTH / 2, 560, fm));

        // Gán hành động khi chọn
        for (LevelButton btn : levelButtons) {
            btn.setActivity(() -> {
                System.out.println("Chọn: " + btn.getText());
                sceneManager.goToGame();
            });
        }
    }

    @Override
    protected void update() {
        // Di chuyển bằng phím
        if (input.isKeyJustPressed(KeyEvent.VK_UP)) {
            selectedIndex = (selectedIndex - 1 + levelButtons.size()) % levelButtons.size();
        }
        if (input.isKeyJustPressed(KeyEvent.VK_DOWN)) {
            selectedIndex = (selectedIndex + 1) % levelButtons.size();
        }
        if (input.isKeyJustPressed(KeyEvent.VK_ENTER)) {
            levelButtons.get(selectedIndex).onClick();
        }

        // Cập nhật trạng thái hover
        for (int i = 0; i < levelButtons.size(); i++) {
            levelButtons.get(i).setHovered(i == selectedIndex);
        }
    }

    @Override
    protected void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(titleFont);
        g.drawString("CHỌN MÀN CHƠI", 250, 80);

        for (LevelButton b : levelButtons) {
            b.draw(g);
        }

        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("↑/↓ để chọn, ENTER để bắt đầu", 250, Constants.HEIGHT - 60);
    }
}
