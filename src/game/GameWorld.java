package game;

import core.InputHandler;
import core.AudioService;
import data.SkinData;
import entity.*;
import entity.Ball;
import game.core.*;
import game.collision.CollisionSystem;
import game.skill.effect.SkillEffectManager;
import game.skill.SkillManager;
import utils.Constants;

import java.awt.*;
import java.util.List; // Thêm import

public class GameWorld {
    private final EntityManager entities;
    private final CollisionProcessor collisionProcessor;
    private final SkillManager skillManager;
    private final SkillEffectManager skillEffectManager;
    private final ScoreSystem scoreSystem;

    private final LevelManager levelManager;
    private final CollisionSystem collisionSystem; // Giữ tham chiếu để reset

    private int breakableBrickCount = 0;
    private boolean isLevelWon = false;

    public GameWorld(InputHandler input) {
        GameInitializer init = new GameInitializer();
        init.initialize(input);

        this.entities = init.getEntityManager();
        this.collisionSystem = init.getCollisionSystem(); // <-- Lấy tham chiếu này
        this.skillManager = init.getSkillManager();
        this.skillEffectManager = init.getSkillEffectManager();
        this.scoreSystem = init.getScoreSystem();
        this.levelManager = init.getLevelManager(); // <-- Lấy LevelManager
        // collision processor wired with orbSpawner
        this.collisionProcessor = new CollisionProcessor(collisionSystem, init.getOrbSpawner(), entities, scoreSystem);
    }

    /**
     * @param levelPath Đường dẫn đến file JSON của level
     * @return Trả về LevelData đã được tải (hoặc null nếu lỗi)
     */
    public LevelData resetAndLoadLevel(String levelPath) {
        // 1. Tải dữ liệu level mới
        LevelData level = levelManager.load(levelPath);
        if (level == null) {
            System.err.println("KHÔNG THỂ TẢI LEVEL: " + levelPath);
            return null; // <-- Trả về null nếu lỗi
        }

        // 2. Dọn dẹp trạng thái cũ
        this.isLevelWon = false;
        this.breakableBrickCount = 0;

        scoreSystem.reset();
        entities.getBalls().clear();
        entities.getManaOrbs().clear();
        skillManager.reset();
        skillEffectManager.clearEffects();

        List<Brick> oldBricks = entities.getBricks();
        for (Brick b : oldBricks) {
            collisionSystem.unregister(b);
        }
        oldBricks.clear();

        // 3. Tải dữ liệu level mới
        List<Brick> newBricks = LevelBuilder.buildBricks(level);
        entities.setBricks(newBricks);

        // 4. Đăng ký gạch mới và đếm
        for (Brick b : newBricks) {
            collisionSystem.register(b);
            if (b.getType() != Brick.Type.BEDROCK) {
                this.breakableBrickCount++;
            }
        }

        // 5. Reset lại bóng
        entities.spawnNewBallAtPaddle();

        return level; // <-- Trả về level đã tải thành công
    }

    public void update(float deltaTime) {
        if (isLevelWon) return;
        skillManager.update(deltaTime);
        entities.updateAll(deltaTime);

        collisionProcessor.processCollisions();
        entities.getManaOrbs().stream()
                .filter(orb -> !orb.isAlive()) // already picked
                .forEach(orb -> scoreSystem.addMana(orb.getManaAmount()));
        entities.cleanupDestroyed();

        if (entities.noBallsRemaining()) {
            scoreSystem.loseLife();
            if(scoreSystem.isGameOver()) {
                // thua ko làm gì
                return;
            }
            else {
                AudioService.playSound("lost_health.wav");
                entities.spawnNewBallAtPaddle();
            }
        }

        if (this.breakableBrickCount > 0) {
            // Đếm số gạch CÓ THỂ PHÁ còn sống
            long liveBreakableBricks = entities.getBricks().stream()
                    .filter(b -> b.getType() != Brick.Type.BEDROCK && b.isAlive())
                    .count();

            if (liveBreakableBricks == 0) {
                this.isLevelWon = true; // Đặt cờ thắng
                System.out.println("LEVEL WON!");
            }
        }

        skillEffectManager.update(deltaTime);
    }

    public void render(Graphics2D g2) {
        LevelData current = levelManager.getCurrentLevel(); // Lấy level đang hoạt động

        // 1. VẼ HÌNH NỀN
        if (current != null && current.background != null) {
            // Vẽ ảnh nền lên toàn bộ màn hình game (Constants.GAME_PANEL_WIDTH, GAME_PANEL_HEIGHT)
            g2.drawImage(current.background,
                    0, 0,
                    Constants.GAME_PANEL_WIDTH, Constants.GAME_PANEL_HEIGHT,
                    null);
        } else {
            // Nếu không có ảnh nền, vẽ màu nền mặc định (ví dụ: đen)
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, Constants.GAME_PANEL_WIDTH, Constants.GAME_PANEL_HEIGHT);
        }
        entities.render(g2);
        skillEffectManager.draw(g2);
    }

    public void forceUpdateGameAssets(SkinData ballSkin, SkinData paddleSkin) {
        // refresh static cache
        Ball.setSkin(ballSkin);
        Paddle.setSkin(paddleSkin);

        // update existing instances
        for (Ball ball : entities.getBalls()) {
            if (ball != null) {
                ball.setImg(Ball.equippedBallImage);
                ball.setColor(Ball.equippedBallColor);
            }
        }
        Paddle paddle = entities.getPaddle();
        if (paddle != null) {
            paddle.setImg(Paddle.equippedPaddleImage);
            paddle.setColor(Paddle.equippedPaddleColor);
        }
    }

    public boolean isGameOver() {
        return scoreSystem.isGameOver();
    }

    public ScoreSystem getScoreSystem() {
        return scoreSystem;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public boolean isLevelWon() {
        return isLevelWon;
    }
}
