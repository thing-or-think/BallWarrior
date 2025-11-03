package ui.scene;

import game.LevelData;
import core.InputHandler;
import core.SceneManager;
import core.ResourceLoader;
import data.GameData;
import data.PlayerData;
import game.GameWorld;
import game.skill.ui.SkillPanel;
import ui.HUD;
import ui.base.Scene;
import ui.panel.GamePanel;
import utils.Constants;
import ui.LeaderboardDisplay;
import java.awt.image.BufferedImage;

import java.awt.*;

public class GameScene extends Scene {
    private final GameWorld world;
    private final HUD hud;
    private final SceneManager sceneManager;
    private final PlayerData playerData;
    private final SkillPanel skillPanel;
    private final GamePanel gamePanel;
    private final LeaderboardDisplay leaderboardDisplay;

    private final GameData gameData;
    private String currentLevelPath;
    private boolean scoreSaved = false;
    private BufferedImage background;

    public GameScene(InputHandler input, SceneManager sceneManager, PlayerData playerData, GameData gameData) {
        super("Game", input);
        this.sceneManager = sceneManager;
        this.playerData = playerData;
        this.gameData = gameData; // <-- LƯU THAM CHIẾU
        this.world = new GameWorld(input);
        this.hud = new HUD(world.getScoreSystem());
        this.skillPanel = new SkillPanel(world.getSkillManager(), 20,  100);
        this.gamePanel = new GamePanel(world);
        this.leaderboardDisplay = new LeaderboardDisplay();

        //setBackground(Color.decode("#212121"));
        background = ResourceLoader.loadImage("assets/images/Bg/gamescene.png");
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

    /**
     * [MỚI] Phương thức để bắt đầu/tải một màn chơi cụ thể
     * @param levelPath Đường dẫn file level
     */
    public void startGame(String levelPath) {
        this.currentLevelPath = levelPath;
        this.scoreSaved = false;

        // [SỬA] Lấy về đối tượng LevelData khi reset
        LevelData loadedLevel = world.resetAndLoadLevel(levelPath);

        // [SỬA] Cập nhật Leaderboard UI với tên thật
        if (loadedLevel != null) {
            // Sử dụng tên "đẹp" từ file JSON (vd: "Level 1: Classic Wall")
            leaderboardDisplay.updateData(levelPath, gameData, loadedLevel.name);
        } else {
            // Fallback (dự phòng) nếu level tải lỗi
            String levelName = levelPath.substring(levelPath.lastIndexOf('/') + 1);
            leaderboardDisplay.updateData(levelPath, gameData, levelName);
        }
    }
    /**
     * Lưu điểm số hiện tại vào Leaderboard
     */
    public void saveCurrentScore() {
        if (scoreSaved) return; // Nếu đã lưu rồi thì bỏ qua

        int finalScore = world.getScoreSystem().getScore();
        sceneManager.updateLeaderboard(currentLevelPath, playerData.getPlayerName(), finalScore);
        scoreSaved = true; // Đánh dấu đã lưu
    }

    @Override
    protected void update() {
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ESCAPE)) {
            sceneManager.goToPause();
            return;
        }

        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_TAB)) {
            leaderboardDisplay.toggleVisibility();
        }

        // Nếu game đã kết thúc (thắng/thua) thì không update world nữa
        if (world.isLevelWon() || world.isGameOver()) {
            return;
        }

        world.update(deltaTime);

        // Kiểm tra THẮNG
        if (world.isLevelWon()) {
            saveCurrentScore(); // <-- LƯU ĐIỂM
            sceneManager.goToWinScene();
            return;
        }

        // Kiểm tra THUA
        if (world.isGameOver()) {
            saveCurrentScore(); // <-- LƯU ĐIỂM
            sceneManager.goToGameOver();
            return;
        }
    }

    @Override
    protected void render(Graphics2D g2) {
        g2.drawImage(background,0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);
        hud.render(g2);
        skillPanel.draw(g2);
        leaderboardDisplay.draw(g2);
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
