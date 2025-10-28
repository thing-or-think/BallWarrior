package ui.scene;

import core.InputHandler;
import core.SceneManager;
import data.PlayerData;
import game.GameWorld;
import game.skill.ui.SkillPanel;
import ui.HUD;
import ui.base.Scene;
import ui.panel.GamePanel;
import utils.Constants;

import java.awt.*;

public class GameScene extends Scene {
    private final GameWorld world;
    private final HUD hud;
    private final SceneManager sceneManager;
    private final PlayerData playerData;
    private final SkillPanel skillPanel;
    private final GamePanel gamePanel;

    public GameScene(InputHandler input, SceneManager sceneManager, PlayerData playerData) {
        super("Game", input);
        this.sceneManager = sceneManager;
        world = new GameWorld(input);
        hud = new HUD(world.getScoreSystem());
        this.playerData = playerData;
        skillPanel = new SkillPanel(world.getSkillManager(), 20,  100);
        gamePanel = new GamePanel(world);
        setBackground(Color.decode("#212121"));
        initUI();
    }

    @Override
    protected void initUI() {
        setLayout(null);
        gamePanel.setBounds(
                (Constants.WINDOW_WIDTH - Constants.GAME_PANEL_WIDTH) / 2,
                (Constants.WINDOW_HEIGHT - Constants.GAME_PANEL_HEIGHT) / 2,
                Constants.GAME_PANEL_WIDTH,
                Constants.GAME_PANEL_HEIGHT
        );
        add(gamePanel);
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
