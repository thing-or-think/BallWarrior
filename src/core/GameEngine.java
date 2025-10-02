package core;

import game.GameScene;
import ui.MenuScene;
import ui.ShopScene;
import utils.Constants;

import javax.swing.*;
import java.awt.*;

public class GameEngine {
    private final JFrame frame;
    private final InputHandler input;
    private final SceneManager sceneManager;
    private final GameLoop gameLoop;

    public GameEngine(JFrame frame) {
        this.frame = frame;
        this.input = new InputHandler();

        // Khởi tạo scene manager
        this.sceneManager = new SceneManager(frame, input);

        // Khởi tạo game scene
        GameScene gameScene = new GameScene(input);
        sceneManager.setGameScene(gameScene);

        // Khởi tạo menu scene
        MenuScene menuScene = new MenuScene(
                input,
                this::startGame,
                this::openShop,
                () -> System.out.println("Inventory!"),
                () -> System.exit(0)
        );
        menuScene.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        sceneManager.setMenuScene(menuScene);

        // Khởi tạo game loop
        this.gameLoop = new GameLoop(gameScene, sceneManager);
    }

    /** Bắt đầu game */
    private void startGame() {
        input.resetMouse();
        sceneManager.showGameScene();
        gameLoop.start();
    }

    /** Mở shop */
    private void openShop() {
        input.resetMouse();
        ShopScene shopScene = new ShopScene(input, sceneManager::showMenuScene);
        sceneManager.showShopScene(shopScene);
    }

    /** Cho Main gọi để mở menu lần đầu */
    public JPanel getMenuScene() {
        return sceneManager.getMenuScene();
    }
}
