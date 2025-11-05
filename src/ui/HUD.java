package ui;

import game.ScoreSystem;
import ui.element.Label;
import ui.element.ComboLabel;
import ui.element.HealthBar;
import utils.Constants;

import java.awt.*;

public class HUD {

    private static final Font FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font COMBO_FONT = new Font("Arial", Font.BOLD, 35);

    private final Label scoreLabel;
    private final HealthBar healthBar; // KHAI BÁO HEALTH BAR
    private final ComboLabel comboLabel;
    private final Label manaLabel;

    private final ScoreSystem scoreSystem;

    // Giả định: Máu tối đa của người chơi (số mạng tối đa)
    private static final int MAX_LIVES = 3;

    public HUD(ScoreSystem scoreSystem) {
        this.scoreSystem = scoreSystem;

        scoreLabel = new Label("Score: 0", 20, 30, FONT, Color.WHITE);
        manaLabel = new Label("Mana: ", Constants.WINDOW_WIDTH - 80, 50, FONT, Color.WHITE);
        // KHỞI TẠO HEALTH BAR
        // Vị trí vẽ ở góc trên bên phải
        healthBar = new HealthBar(MAX_LIVES, null);

        // combo ở giữa bên phải màn hình
        comboLabel = new ComboLabel(Constants.WINDOW_WIDTH - 100, Constants.WINDOW_HEIGHT / 2, COMBO_FONT);
    }

    /**
     * Cập nhật nội dung và hiển thị HUD.
     */
    public void render(Graphics2D g) {
        // Cập nhật text mỗi frame
        scoreLabel.setText("Score: " + scoreSystem.getScore());
        manaLabel.setText("Mana: " + scoreSystem.getMana());
        healthBar.setCurrentHealth(scoreSystem.getLives());
        comboLabel.setComboValue(scoreSystem.getCombo());

        // 2. VẼ HEALTH BAR
        // Vị trí vẽ Health Bar (ví dụ: góc trên bên phải, cùng dòng với Score)
        healthBar.draw(g, Constants.WINDOW_WIDTH - 120, 15);
        // Vẽ nhãn
        scoreLabel.draw(g);
        comboLabel.draw(g);
        manaLabel.draw(g);
    }
}
