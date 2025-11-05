package core;

import core.AudioService;
import data.GameData;
import data.LeaderboardEntry;
import data.PlayerData;
import ui.scene.ShopScene;
import ui.base.Scene;
import ui.scene.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SceneManager {

    private final JFrame frame;
    private final InputHandler input;

    private GameData gameData;
    private PlayerData currentPlayer;

    private GameScene gameScene;
    private MenuScene menuScene;
    private ShopScene shopScene;
    private InstructionScene instructionScene;
    private OwnedScene ownedScene;
    private PauseScene pauseScene;
    private LevelSelectScene levelSelectScene;
    private GameOverScene gameOverScene;
    private WinScene winScene;
    private PlayerSelectScene playerSelectScene;

    private Scene currentScene;

    public SceneManager(JFrame frame, InputHandler input) {
        this.frame = frame;
        this.input = input;

        this.gameData = ResourceLoader.loadGameData();

        loadCurrentPlayer();
        reloadApplicationState(false);

        // [SỬA LỖI 1 - PHẦN SAVE]
        // Thêm hook để lưu GameData khi tắt
        SceneManager self = this;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ResourceSaver.saveGameData(self.gameData);
            System.out.println("GameData saved on exit!");
        }));
        // --- KẾT THÚC SỬA LỖI 1 ---

        setScene(menuScene);
    }

    private void loadCurrentPlayer() {
        String currentName = gameData.getCurrentPlayerName();

        if (currentName != null) {
            this.currentPlayer = gameData.getAllPlayers().stream()
                    .filter(p -> p.getPlayerName().equals(currentName))
                    .findFirst()
                    .orElse(null);
        }

        if (this.currentPlayer == null) {
            if (gameData.getAllPlayers().isEmpty()) {
                System.out.println("Không có người chơi nào, tạo 'Player 1'");
                // [SỬA LỖI 2]
                PlayerData template = ResourceLoader.loadPlayerDataTemplate(); // <-- Sửa ở đây
                this.currentPlayer = new PlayerData(template);
                this.currentPlayer.setPlayerName("Player 1");
                this.currentPlayer.setCoins(1000);
                gameData.getAllPlayers().add(this.currentPlayer);
            } else {
                this.currentPlayer = gameData.getAllPlayers().get(0);
            }
            gameData.setCurrentPlayerName(this.currentPlayer.getPlayerName());
            ResourceSaver.saveGameData(gameData);
        }
        System.out.println("Đăng nhập với tư cách: " + this.currentPlayer.getPlayerName());
    }

    public void reloadApplicationState() {
        reloadApplicationState(true);
    }

    private void reloadApplicationState(boolean doSetScene) {
        loadCurrentPlayer();
        ownedScene = new OwnedScene(input,this);
        shopScene = new ShopScene(input, currentPlayer,ownedScene,this);
        instructionScene = new InstructionScene(input,this);
        gameScene = new GameScene(input, this, currentPlayer, gameData);
        menuScene = new MenuScene(
                input,
                currentPlayer.getPlayerName(),
                this::goToLevelSelect,
                this::goToShop,
                this::goToPlayerSelect,
                this::gotoInstruction,
                () -> System.exit(0)
        );

        pauseScene = new PauseScene(input, this, gameScene);
        levelSelectScene = new LevelSelectScene(input, this);
        gameOverScene = new GameOverScene(input, this::goToMenu);
        winScene = new WinScene(input, this::goToLevelSelect, this::goToMenu);
        playerSelectScene = new PlayerSelectScene(input, this, gameData);

        shopScene.setOnBack(() -> setScene(menuScene));

        if (doSetScene) {
            setScene(menuScene);
        }
    }

    // --- Phương thức quản lý Người chơi (Goal 1) ---

    public void addPlayer(String newName) {
        boolean isDuplicate = gameData.getAllPlayers().stream()
                .anyMatch(p -> p.getPlayerName().equalsIgnoreCase(newName));
        if (isDuplicate) {
            throw new IllegalArgumentException("Tên người chơi đã tồn tại.");
        }

        // [SỬA LỖI 3]
        PlayerData template = ResourceLoader.loadPlayerDataTemplate(); // <-- Sửa ở đây
        PlayerData newPlayer = new PlayerData(template);
        newPlayer.setPlayerName(newName);
        newPlayer.setCoins(1000);

        gameData.getAllPlayers().add(newPlayer);
        ResourceSaver.saveGameData(gameData);
    }

    public void deletePlayer(String playerName) {
        gameData.getAllPlayers().removeIf(p -> p.getPlayerName().equals(playerName));

        if (gameData.getCurrentPlayerName().equals(playerName)) {
            if (gameData.getAllPlayers().isEmpty()) {
                addPlayer("Player 1");
            }
            gameData.setCurrentPlayerName(gameData.getAllPlayers().get(0).getPlayerName());
        }
        ResourceSaver.saveGameData(gameData);
    }

    // --- Phương thức quản lý Leaderboard (Goal 2) ---
    public void updateLeaderboard(String levelPath, String playerName, int score) {
        if (score == 0) return;

        List<LeaderboardEntry> scores = gameData.getLeaderboards().get(levelPath);
        if (scores == null) {
            scores = new ArrayList<>();
            gameData.getLeaderboards().put(levelPath, scores);
        }

        LeaderboardEntry existingEntry = scores.stream()
                .filter(s -> s.playerName.equals(playerName))
                .findFirst()
                .orElse(null);

        if (existingEntry != null) {
            if (score > existingEntry.score) {
                existingEntry.score = score;
                System.out.println("Leaderboard: Cập nhật điểm cao mới cho " + playerName + " tại " + levelPath);
                ResourceSaver.saveGameData(gameData);
            }
        } else {
            scores.add(new LeaderboardEntry(playerName, score));
            System.out.println("Leaderboard: Thêm điểm mới cho " + playerName + " tại " + levelPath);
            ResourceSaver.saveGameData(gameData);
        }
    }


    // --- Các phương thức điều hướng (Giữ nguyên và thêm mới) ---

    public void setScene(Scene scene) {
        if (currentScene != null) {
            if (currentScene instanceof PlayerSelectScene) {
                currentScene.removeAll();
            }
            currentScene.stopRepaintLoop();
            frame.remove(currentScene);
        }

        currentScene = scene;
        frame.setContentPane(currentScene);

        if (currentScene instanceof PlayerSelectScene) {
            frame.revalidate();
            frame.repaint();
            currentScene.requestFocusInWindow();
        } else {
            frame.revalidate();
            frame.repaint();
        }

        currentScene.startRepaintLoop();
    }

    public Scene getCurrentScene() { return currentScene; }
    public void goToMenu() { setScene(menuScene); }

    public void goToGame(String levelPath) {
        gameScene.startGame(levelPath);
        gameScene.forceUpdateGameAssets();
        setScene(gameScene);
    }

    public void goToPasuseMenu(GameScene gameScene) {
        pauseScene.setGameScene(gameScene);
        setScene(pauseScene);
    }

    public void goToGame() { goToGame("assets/levels/level1.json"); }
    public void gotoOwned() { setScene(ownedScene); }
    public void goToShop() { setScene(shopScene); }
    public void gotoInstruction() { setScene(instructionScene); }
    public void goToPause() { setScene(pauseScene); }
    public void goToLevelSelect() { setScene(levelSelectScene); }
    public void goToGameOver() {
        AudioService.playSound("game_over.wav");
        setScene(gameOverScene);
    }
    public void goToWinScene() {
        AudioService.playSound("win.wav");
        setScene(winScene);
    }

    public void backTo(Scene scene) {
        setScene(scene);
    }

    public void goToPlayerSelect() {
        // [SỬA LỖI 4]
        // Gọi phương thức public, không phải private
        playerSelectScene.refreshPlayerList();
        setScene(playerSelectScene);
    }
}
