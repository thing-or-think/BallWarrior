package ui;

import game.ScoreSystem;
import ui.element.ComboLabel;
import ui.element.Label;
import utils.Constants;

import java.awt.*;

/**
 * HUD (Heads-Up Display)
 * ---------------------------------------------------------------
 * Hiển thị thông tin người chơi: điểm, mạng, combo hiện tại.
 * Dựa trên dữ liệu từ ScoreSystem.
 * ---------------------------------------------------------------
 */
public class HUD {

    private static final Font FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font COMBO_FONT = new Font("Arial", Font.BOLD, 35);

    private final Label scoreLabel;
    private final Label livesLabel;
    private final ComboLabel comboLabel;

    private final ScoreSystem scoreSystem;

    public HUD(ScoreSystem scoreSystem) {
        this.scoreSystem = scoreSystem;

        scoreLabel = new Label("Score: 0", 20, 30, FONT, Color.WHITE);
        livesLabel = new Label("Lives: 0", Constants.WIDTH - 80, 30, FONT, Color.WHITE);

        // combo ở giữa bên phải màn hình
        comboLabel = new ComboLabel(Constants.WIDTH - 100, Constants.HEIGHT / 2, COMBO_FONT);
    }

    /**
     * Cập nhật nội dung và hiển thị HUD.
     */
    public void render(Graphics2D g) {
        // cập nhật nội dung
        scoreLabel.setText("Score: " + scoreSystem.getScore());
        livesLabel.setText("Lives: " + scoreSystem.getLives());

        // cập nhật combo label (tự xử lý hiệu ứng bên trong)
        comboLabel.setComboValue(scoreSystem.getCombo());

        // vẽ các label
        scoreLabel.draw(g);
        livesLabel.draw(g);
        comboLabel.draw(g);
    }
}
