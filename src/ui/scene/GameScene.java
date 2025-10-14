package ui.scene;

import core.InputHandler;
import core.ResourceLoader;
import core.SceneManager;
import data.PlayerData;
import entity.Ball;
import entity.Paddle;
import game.GameWorld;
import ui.HUD;
import ui.base.Scene;
import java.awt.Graphics2D;

public class GameScene extends Scene {
    private GameWorld world;
    private HUD hud;
    private SceneManager sceneManager;
    private final PlayerData playerData;

    public GameScene(InputHandler input, SceneManager sceneManager) {
        super("Game", input);
        this.sceneManager = sceneManager;
        world = new GameWorld(input);
        hud = new HUD(world.getScoreSystem());
        playerData = new PlayerData(ResourceLoader.loadPlayerData());
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void update() {
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ESCAPE)) {
            sceneManager.goToPause();
            return;
        }
        world.update(deltaTime);
    }

    @Override
    protected void render(Graphics2D g2) {
        world.render(g2);
        hud.render(g2);
    }

    public void forceUpdateGameAssets() {
        // Tải lại Skin vào cache static
        world.forceUpdateGameAssets();
        playerData.setPlayerData(ResourceLoader.loadPlayerData());
        System.out.println(playerData.getCoins());
    }
}
