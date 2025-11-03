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

    public void backTo(Scene scene) {
        setScene(scene);
    }


    public Scene getCurrentScene() {
        return currentScene;
    }

    public void goToMenu() {
        setScene(menuScene);
    }

    //Sửa đổi goToGame để nhận đường dẫn level
    public void goToGame(String levelPath) {
        gameScene.startGame(levelPath); // Yêu cầu GameScene chuẩn bị level
        setScene(gameScene);
        gameScene.forceUpdateGameAssets();
    }

    public void goToGame() {
        goToGame("assets/levels/level1.json"); // Mặc định vào level 1
    }

    public void goToShop() {
        setScene(shopScene);
    }

    //THÊM HÀM PAUSE SCENE
    public void goToPauseMenu(GameScene gameScene) {
        pauseScene.setGameScene(gameScene);
        setScene(pauseScene);
    }

    public void goToPause() { setScene(pauseScene); }

    public void goToLevelSelect() { setScene(levelSelectScene); }

    public void goToGameOver() {
        setScene(new GameOverScene(input, this::goToMenu));
    }
}
