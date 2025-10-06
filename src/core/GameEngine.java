package core;

import game.GameScene;
import ui.scene.MenuScene;
import ui.scene.ShopScene;
import utils.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * GameEngine quản lý khởi tạo và điều hướng giữa các scene:
 * - MenuScene, ShopScene (thuộc hệ SceneManager)
 * - GameScene (tự có game loop riêng)
 */
public class GameEngine {

    private final JFrame frame;
    private final InputHandler input;
    private final SceneManager sceneManager;
    private final GameLoop gameLoop;

    private final GameScene gameScene;
    private final MenuScene menuScene;

    // ==== CONSTRUCTOR ========================================================

    public GameEngine(JFrame frame) {
        this.frame = frame;
        this.input = new InputHandler();

        // Khởi tạo SceneManager để quản lý scene giao diện
        this.sceneManager = new SceneManager(null);

        // ==== Khởi tạo GameScene (có vòng lặp riêng) ====
        this.gameScene = new GameScene(input);
        this.gameLoop = new GameLoop(gameScene);

        // ==== Khởi tạo MenuScene ====
        this.menuScene = new MenuScene(
                input,
                this::startGame,             // Bắt đầu game
                this::openShop,              // Mở shop
                () -> System.out.println("Inventory!"),
                () -> System.exit(0)
        );

        menuScene.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));

        // ==== Thiết lập frame chính ====
        frame.setTitle("My Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(sceneManager);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Bắt đầu bằng Menu
        sceneManager.setScene(menuScene);
    }

    // ==== CHUYỂN SCENE =======================================================

    /** Bắt đầu game: chuyển sang GameScene + chạy game loop riêng */
    private void startGame() {
        input.resetMouse();

        SwingUtilities.invokeLater(() -> {
            frame.getContentPane().remove(sceneManager);
            frame.getContentPane().add(new GamePanel(gameScene));
            frame.revalidate();
            frame.repaint();

            gameLoop.start(); // Bắt đầu vòng game riêng
        });
    }

    /** Mở shop từ menu */
    private void openShop() {
        input.resetMouse();
        ShopScene shopScene = new ShopScene(input, () -> sceneManager.setScene(menuScene));
        sceneManager.setScene(shopScene);
    }

    /** Cho Main gọi để mở menu lần đầu */
    public JPanel getMenuScene() {
        return menuScene;
    }
}
