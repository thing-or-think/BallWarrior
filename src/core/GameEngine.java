package core;

import game.GameScene;
import ui.MenuScene;
import ui.ShopScene;
import ui.GameOverScene;
import utils.Constants;

import javax.swing.*;
import java.awt.*;

public class GameEngine {
    private final JFrame frame;
    private final InputHandler input;
    private final SceneManager sceneManager;
    private GameLoop gameLoop;

    // Các biến lưu callbacks để tránh tái tạo lambda liên tục
    private final Runnable onPlayGameCallback;
    private final Runnable onOpenShopCallback;
    private final Runnable onOpenInventoryCallback;
    private final Runnable onQuitGameCallback;
    private final Runnable onShowMainMenuCallback;
    private final Runnable onGameIsOverCallback;

    public GameEngine(JFrame frame) {
        this.frame = frame;
        this.input = new InputHandler();

        // Khởi tạo scene manager
        this.sceneManager = new SceneManager(frame, input);

        // --- Định nghĩa tất cả các callbacks trước ---
        this.onPlayGameCallback = this::startGame;
        this.onOpenShopCallback = this::openShop;
        this.onOpenInventoryCallback = () -> System.out.println("Inventory!");
        this.onQuitGameCallback = () -> System.exit(0);
        this.onShowMainMenuCallback = this::showMainMenu;
        this.onGameIsOverCallback = this::onGameOver;

        // Khởi tạo menu scene
        MenuScene menuScene = new MenuScene(
                input,
                onPlayGameCallback,
                onOpenShopCallback,
                onOpenInventoryCallback,
                onQuitGameCallback
        );
        menuScene.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        sceneManager.setMenuScene(menuScene);

        // Khởi tạo game scene, truyền callback onGameIsOver
        GameScene gameScene = new GameScene(input, onGameIsOverCallback);
        sceneManager.setGameScene(gameScene);

        // Khởi tạo game over scene, truyền callback onShowMainMenu
        GameOverScene gameOverScene = new GameOverScene(input, onShowMainMenuCallback);
        sceneManager.setGameOverScene(gameOverScene);

        initializeGameComponents();
    }

    /** Phương thức để khởi tạo hoặc reset GameScene và GameLoop */
    private void initializeGameComponents() {
        // Luôn tạo một GameScene mới khi chuẩn bị chơi
        GameScene gameScene = new GameScene(input, onGameIsOverCallback);
        sceneManager.setGameScene(gameScene); // Cập nhật GameScene cho SceneManager

        // Nếu gameLoop đã tồn tại và đang chạy, dừng nó trước.
        if (gameLoop != null && gameLoop.isRunning()) {
            gameLoop.stop();
        }
        // Tạo GameLoop mới hoặc cập nhật GameScene cho GameLoop hiện có
        this.gameLoop = new GameLoop(gameScene, sceneManager);
    }

    /** Bắt đầu game */
    private void startGame() {
        input.resetMouse(); // Reset trạng thái chuột

        // Luôn đảm bảo GameScene và GameLoop được reset và khởi tạo lại trước khi bắt đầu
        initializeGameComponents(); // Reset GameScene và GameLoop về trạng thái ban đầu

        sceneManager.showGameScene();
        gameLoop.start(); // Bắt đầu GameLoop với GameScene mới
        System.out.println("Game started."); // Debug
    }

    /** Mở shop */
    private void openShop() {
        input.resetMouse();
        ShopScene shopScene = new ShopScene(input, onShowMainMenuCallback); // Dùng callback đã định nghĩa
        sceneManager.showShopScene(shopScene);
        System.out.println("Shop opened."); // Debug
    }

    /** Hiển thị menu chính */
    private void showMainMenu() {
        input.resetMouse();
        sceneManager.showMenuScene();
        // Sau khi quay về main menu, dừng gameLoop hiện tại (nếu đang chạy)
        // và chuẩn bị cho một ván game mới khi nhấn PLAY
        if (gameLoop != null && gameLoop.isRunning()) {
            gameLoop.stop();
        }
        System.out.println("Returned to Main Menu."); // Debug
    }

    /** Xử lý khi game over */
    private void onGameOver() {
        System.out.println("Entering onGameOver method in GameEngine."); // Debug

        // 1. Dừng GameLoop trước khi làm bất cứ điều gì khác
        if (gameLoop != null && gameLoop.isRunning()) {
            gameLoop.stop();
            System.out.println("GameLoop stopped."); // Debug
        }
        input.resetMouse(); // Reset trạng thái chuột

        // 2. Hiển thị màn hình Game Over
        sceneManager.showGameOverScene();
        System.out.println("Displayed GameOverScene."); // Debug

        // 3. KHÔNG tạo GameScene mới ở đây.
        // Việc reset GameScene và GameLoop sẽ được thực hiện khi người chơi nhấn "PLAY" từ Menu chính.
        // Điều này đảm bảo rằng GameLoop mới sẽ làm việc với một GameScene mới hoàn toàn.
    }

    /** Cho Main gọi để mở menu lần đầu */
    public JPanel getMenuScene() {
        return sceneManager.getMenuScene();
    }
}
