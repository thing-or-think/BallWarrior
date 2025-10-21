package core;

import data.PlayerData;
import ui.scene.ShopScene;
import ui.base.Scene;
import ui.scene.*;

import javax.swing.*;

public class SceneManager {

    private final JFrame frame;
    private final InputHandler input;

    private final GameScene gameScene;
    private final MenuScene menuScene;
    private final ShopScene shopScene;
    private final PauseScene pauseScene;
    private final LevelSelectScene levelSelectScene;
    private final GameOverScene gameOverScene;

    private Scene currentScene;

    public SceneManager(JFrame frame, InputHandler input) {
        this.frame = frame;
        this.input = input;

        PlayerData playerData = new PlayerData(ResourceLoader.loadPlayerData());

        shopScene = new ShopScene(input, playerData); // tạm null
        gameScene = new GameScene(input, this, playerData);
        menuScene = new MenuScene(
                input,
                this::goToLevelSelect,
                this::goToShop,
                () -> System.out.println("Inventory!"),
                () -> System.exit(0)
        );
        pauseScene = new PauseScene(input, this, gameScene);
        levelSelectScene = new LevelSelectScene(input, this);

        shopScene.setOnBack(() -> setScene(menuScene));
        gameOverScene = new GameOverScene(input, this::goToMenu);

        // 3️⃣ Bắt đầu từ menu
        setScene(menuScene);
    }

    public void setScene(Scene scene) {
        if (currentScene != null) {
            currentScene.stopRepaintLoop();
            frame.remove(currentScene);
        }

        currentScene = scene;
        frame.setContentPane(currentScene);
        frame.revalidate();
        frame.repaint();
        currentScene.startRepaintLoop();
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void goToMenu() {
        setScene(menuScene);
    }

    public void goToGame() {
        gameScene.forceUpdateGameAssets();
        setScene(gameScene);
    }

    public void goToShop() {
        setScene(shopScene);
    }

    public void goToPause() { setScene(pauseScene); }

    public void goToLevelSelect() { setScene(levelSelectScene); }

    public void goToGameOver() {
        setScene(gameOverScene);
    }
}
