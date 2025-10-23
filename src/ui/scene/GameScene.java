package ui.scene;

import core.InputHandler;
import core.ResourceLoader;
import core.SceneManager;
import data.PlayerData;
import entity.Ball;
import entity.Paddle;
import game.GameWorld;
import game.skill.ui.SkillPanel;
import ui.HUD;
import ui.base.Scene;
import utils.Constants;

import java.awt.Graphics2D;

public class GameScene extends Scene {
    private GameWorld world;
    private HUD hud;
    private SceneManager sceneManager;
    private final PlayerData playerData;
    private final SkillPanel skillPanel;

    public GameScene(InputHandler input, SceneManager sceneManager, PlayerData playerData) {
        super("Game", input);
        this.sceneManager = sceneManager;
        world = new GameWorld(input);
        hud = new HUD(world.getScoreSystem());
        this.playerData = playerData;
        skillPanel = new SkillPanel(world.getSkillManager(), 20, Constants.HEIGHT - 50);
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

        if (world.isGameOver()) {
            sceneManager.goToGameOver();
        }
    }

    @Override
    protected void render(Graphics2D g2) {
        world.render(g2);
        hud.render(g2);
        skillPanel.draw(g2);
    }

    public void forceUpdateGameAssets() {
        // Tải lại Skin vào cache static
        int skinBallId = playerData.getEquipped().getBallId();
        int skinPaddleId = playerData.getEquipped().getPaddleId();
        world.forceUpdateGameAssets(
                playerData.getInventory().getItems().get(skinBallId),
                playerData.getInventory().getItems().get(skinPaddleId)
        );
        System.out.println(playerData.getCoins());
    }
}
