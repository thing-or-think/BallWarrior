//package ui.scene;
//
//import core.InputHandler;
//import core.SceneManager;
//import ui.base.Button;
//import ui.base.Scene;
//import ui.button.LeftArrowButton;
//import ui.button.PlayButton;
//import ui.button.RightArrowButton;
//import ui.element.Label;
//import utils.Constants;
//
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.util.ArrayList;
//import java.util.List;
//
//public class LevelSelectScene extends Scene {
//
//    private final SceneManager sceneManager;
//    private final List<Button> buttons = new ArrayList<>();
//    private Label titleLabel;
//
//
//    public LevelSelectScene(InputHandler input, SceneManager sceneManager) {
//        super("LevelSelectScene", input);
//        this.sceneManager = sceneManager;
//
//        initUI();
//    }
//
//    @Override
//    protected void initUI() {
//        Font font = new Font("Arial", Font.BOLD, 48);
//        FontMetrics fm = new Canvas().getFontMetrics(font);
//        int textWidth = fm.stringWidth("SELECT LEVEL");
//
//        int centerX = (Constants.WIDTH - textWidth) / 2;
//        titleLabel = new Label("SELECT LEVEL", centerX, 40, font, Color.WHITE);
//
//        buttons.add(new LeftArrowButton(40, 300, 40, 40));
//        buttons.add(new RightArrowButton(720, 300, 40, 40));
//        buttons.add(new PlayButton("Play", 340, 480, 120, 40, new Font("Serif", Font.PLAIN, 32), () -> sceneManager.goToGame()));
//    }
//
//    @Override
//    protected void update() {
//        int mx = input.getMouseX();
//        int my = input.getMouseY();
//        for (Button button : buttons) {
//            button.setHovered(button.contains(mx, my));
//            if (button.isHovered() && input.consumeClick()) {
//                button.onClick();
//            }
//        }
//    }
//
//    @Override
//    protected void render(Graphics2D g) {
//        titleLabel.draw(g);
//
//        for (Button button : buttons) {
//            button.draw(g);
//        }
//    }
//}
