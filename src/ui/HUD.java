package ui;

import game.ScoreSystem;
import ui.element.Label;
import ui.element.ComboLabel;
import utils.Constants;

import java.awt.*;

public class HUD {

    private static final Font FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font COMBO_FONT = new Font("Arial", Font.BOLD, 35);

    private final Label scoreLabel;
    private final Label livesLabel;
    private final ComboLabel comboLabel;
    private final Label manaLabel;

    private final ScoreSystem scoreSystem;

    public HUD(ScoreSystem scoreSystem) {
        this.scoreSystem = scoreSystem;

        scoreLabel = new Label("Score: 0", 20, 30, FONT, Color.WHITE);
        livesLabel = new Label("Lives: 0", Constants.WINDOW_WIDTH - 80, 30, FONT, Color.WHITE);
        manaLabel = new Label("Mana: ", Constants.WINDOW_WIDTH - 80, 50, FONT, Color.WHITE);

        // combo ở giữa bên phải màn hình
        comboLabel = new ComboLabel(Constants.WINDOW_WIDTH - 100, Constants.WINDOW_HEIGHT / 2, COMBO_FONT);
    }

    /**
     * Cập nhật nội dung và hiển thị HUD.
     */
    public void render(Graphics2D g) {
        // Cập nhật text mỗi frame
        scoreLabel.setText("Score: " + scoreSystem.getScore());
        livesLabel.setText("Lives: " + scoreSystem.getLives());
        manaLabel.setText("Mana: " + scoreSystem.getMana());

        // Vẽ nhãn
        scoreLabel.draw(g);
        livesLabel.draw(g);
        comboLabel.draw(g);
        manaLabel.draw(g);
    }
}
