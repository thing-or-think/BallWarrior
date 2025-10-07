package core;

import ui.base.Scene;
import game.GameScene;
import ui.scene.MenuScene;
import ui.scene.PauseScene;
import ui.scene.ShopScene;

import javax.swing.*;

public class SceneManager {

    private final JFrame frame;
    private final InputHandler input;

    private final GameScene gameScene;
    private final MenuScene menuScene;
    private final ShopScene shopScene;
    private final PauseScene pauseScene;

    private Scene currentScene;

    public SceneManager(JFrame frame, InputHandler input) {
        this.frame = frame;
        this.input = input;

        gameScene = new GameScene(input, this);
        shopScene = new ShopScene(input, null); // tạm null
        menuScene = new MenuScene(
                input,
                this::goToGame,
                this::goToShop,
                () -> System.out.println("Inventory!"),
                () -> System.exit(0)
        );

        pauseScene = new PauseScene(input, this, gameScene);

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

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void goToMenu() {
        setScene(menuScene);
    }

    public void goToGame() {
        setScene(gameScene);
    }

    public void goToShop() {
        setScene(shopScene);
    }

    public void goToPause() { setScene(pauseScene); }
}
