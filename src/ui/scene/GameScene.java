package ui.scene;

import core.InputHandler;
import core.SceneManager;
import entity.Ball;
import entity.Paddle;
import game.GameWorld;
import ui.HUD;
import ui.base.Scene;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GameScene extends Scene {
    private GameWorld world;
    private HUD hud;
    private SceneManager sceneManager;

    public GameScene(InputHandler input, SceneManager sceneManager) {
        super("Game", input);
        this.sceneManager = sceneManager;
        world = new GameWorld(input);
        hud = new HUD(world.getScoreSystem());
    }

    @Override
    protected void initUI() {

    }

    /**
     * Phương thức để bắt đầu/tải một màn chơi cụ thể
     * @param levelPath Đường dẫn file level
     */
    public void startGame(String levelPath) {
        world.resetAndLoadLevel(levelPath);
    }

    @Override
    protected void update() {
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ESCAPE)) {
            sceneManager.goToPause();
            return;
        }

        world.update(deltaTime);

        if (world.isGameOver()) {
            sceneManager.goToGameOver();
        }
    }

    @Override
    protected void render(Graphics2D g2) {
        world.render(g2);
        hud.render(g2);
    }

    public void forceUpdateGameAssets() {
        // Tải lại Skin vào cache static
        world.forceUpdateGameAssets();
    }
}
