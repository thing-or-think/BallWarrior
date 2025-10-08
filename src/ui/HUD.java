package ui;

import game.ScoreSystem;
import ui.element.Label;
import utils.Constants;

import java.awt.*;

public class HUD {

    private static final Font FONT = new Font("Arial", Font.BOLD, 16);
    private final Label scoreLabel;
    private final Label livesLabel;

    public HUD() {
        scoreLabel = new Label("Score: 0", 20, 30, FONT, Color.WHITE);
        livesLabel = new Label("Lives: 0", Constants.WIDTH - 80, 30, FONT, Color.WHITE);
    }

    public void render(Graphics2D g, ScoreSystem scoreSystem) {
        // Cập nhật text mỗi frame
        scoreLabel.setText("Score: " + scoreSystem.getScore());
        livesLabel.setText("Lives: " + scoreSystem.getLives());

        // Vẽ nhãn
        scoreLabel.draw(g);
        livesLabel.draw(g);
    }
}
