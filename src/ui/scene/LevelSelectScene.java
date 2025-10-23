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

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class LevelSelectScene extends Scene {

    private final SceneManager sceneManager;
    private final List<Button> buttons = new ArrayList<>();
    private Label titleLabel;


    public LevelSelectScene(InputHandler input, SceneManager sceneManager) {
        super("LevelSelectScene", input);
        this.sceneManager = sceneManager;

        initUI();
    }

    @Override
    protected void initUI() {
        Font font = new Font("Arial", Font.BOLD, 48);
        FontMetrics fm = new Canvas().getFontMetrics(font);
        int textWidth = fm.stringWidth("SELECT LEVEL");

        int centerX = (Constants.WINDOW_WIDTH - textWidth) / 2;
        titleLabel = new Label("SELECT LEVEL", centerX, 40, font, Color.WHITE);

        buttons.add(new LeftArrowButton(40, Constants.WINDOW_HEIGHT / 2, 40, 40));
        buttons.add(new RightArrowButton(Constants.WINDOW_WIDTH - 80, Constants.WINDOW_HEIGHT / 2, 40, 40));
        buttons.add(new PlayButton("Play", Constants.WINDOW_WIDTH / 2 - 50, Constants.WINDOW_HEIGHT - 50, 120, 40, new Font("Serif", Font.PLAIN, 32), () -> sceneManager.goToGame()));
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

    @Override
    protected void render(Graphics2D g) {
        titleLabel.draw(g);

        g.setColor(Color.WHITE);
        g.drawRect(150, 120, Constants.WINDOW_WIDTH - 300, Constants.WINDOW_HEIGHT - 180);

        for (Button button : buttons) {
            button.draw(g);
        }
    }
}
