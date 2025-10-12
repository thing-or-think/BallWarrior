//package ui.scene;
//
//import core.InputHandler;
//import core.SceneManager;
//import ui.base.Button;
//import ui.base.Scene;
//import ui.button.MenuButton;
//import utils.Constants;
//
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PauseScene extends Scene {
//
//    private final SceneManager sceneManager;
//    private final Scene gameScene; // Tham chiếu đến GameScene để resume
//    private final List<Button> buttons = new ArrayList<>();
//
//    public PauseScene(InputHandler input, SceneManager sceneManager, Scene gameScene) {
//        super("PauseScene", input);
//        this.sceneManager = sceneManager;
//        this.gameScene = gameScene;
//        initUI();
//    }
//
//    @Override
//    protected void initUI() {
//        int buttonWidth = 200;
//        int buttonHeight = 50;
//        int startY = Constants.HEIGHT / 2 + 60; // Vị trí nút đầu tiên
//        int gap = 70;
//
//        Font font = new Font("Serif", Font.PLAIN, 32);
//        FontMetrics fm = getFontMetrics(font);
//
//        // ====== Nút Resume ======
//        buttons.add(new MenuButton(
//                "Resume",
//                Constants.WIDTH / 2,
//                startY,
//                fm,
//                () -> sceneManager.goToGame() // Resume lại game
//        ));
//
//        // ====== Nút Menu ======
//        buttons.add(new MenuButton(
//                "Menu",
//                Constants.WIDTH / 2,
//                startY + gap,
//                fm,
//                () -> sceneManager.goToMenu()
//        ));
//
//        // ====== Nút Setting ======
//        buttons.add(new MenuButton(
//                "Setting",
//                Constants.WIDTH / 2,
//                startY + 2 * gap,
//                fm,
//                () -> System.out.println("Setting clicked!") // Tạm placeholder
//        ));
//    }
//
//    @Override
//    protected void update() {
//        // Ấn ESC để quay lại game
//        if (input.isKeyJustPressed(KeyEvent.VK_ESCAPE)) {
//            sceneManager.goToGame();
//        }
//
//        int mx = input.getMouseX();
//        int my = input.getMouseY();
//
//        for (Button button : buttons) {
//            button.setHovered(button.contains(mx, my));
//            if (button.isHovered() && input.consumeClick()) {
//                button.onClick();
//            }
//        }
//    }
//
//    @Override
//    protected void render(Graphics2D g2) {
//        // Vẽ lại GameScene trước
//        gameScene.paint(g2);
//
//        // Lớp phủ mờ
//        g2.setColor(new Color(0, 0, 0, 150));
//        g2.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
//
//        // Tiêu đề "PAUSED"
//        g2.setColor(Color.WHITE);
//        g2.setFont(new Font("Arial", Font.BOLD, 48));
//        String text = "PAUSED";
//        int textWidth = g2.getFontMetrics().stringWidth(text);
//        g2.drawString(text, (Constants.WIDTH - textWidth) / 2, Constants.HEIGHT / 2);
//
//        // Vẽ các nút
//        for (Button button : buttons) {
//            button.draw(g2);
//        }
//    }
//}
