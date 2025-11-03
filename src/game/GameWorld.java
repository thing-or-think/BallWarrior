package game;

import core.InputHandler;
import data.SkinData;
import entity.*;
import entity.Ball;
import game.core.*;
import game.collision.CollisionSystem;
import game.skill.effect.SkillEffectManager;
import game.skill.SkillManager;
import game.SoundManager;
import game.LevelData;

import java.awt.*;
import java.util.List;

public class GameWorld {
    private final EntityManager entities;
    private final CollisionProcessor collisionProcessor;
    private final SkillManager skillManager;
    private final SkillEffectManager skillEffectManager;
    private final ScoreSystem scoreSystem;
    private final SoundManager soundManager;
    private final LevelManager levelManager;
    private final CollisionSystem collisionSystem;

    public GameWorld(InputHandler input) {
        GameInitializer init = new GameInitializer();
        init.initialize(input);

        this.entities = init.getEntityManager();
        this.skillManager = init.getSkillManager();
        this.skillEffectManager = init.getSkillEffectManager();
        this.scoreSystem = init.getScoreSystem();
        this.soundManager = init.getSoundManager();
        this.collisionSystem = init.getCollisionSystem();
        this.levelManager = init.getLevelManager();

        // collision processor wired with orbSpawner
        this.collisionProcessor = new CollisionProcessor(
                this.collisionSystem,
                init.getOrbSpawner(),
                entities,
                scoreSystem,
                soundManager
        );
    }

    /**
     * Phương thức để reset và tải một màn chơi mới
     * @param levelPath Đường dẫn đến file JSON của level
     */
    public void resetAndLoadLevel(String levelPath) {
        // 1. Dọn dẹp trạng thái cũ
        scoreSystem.reset();
        entities.resetLevelEntities();
        collisionSystem.clear();

        // 2. Tải dữ liệu level mới
        LevelData level = levelManager.loadLevelByPath(levelPath);
        if (level != null) {
            List<Brick> newBricks = LevelBuilder.buildBricks(level);
            entities.setBricks(newBricks);

            collisionSystem.register(entities.getPaddle());

            // 3. Đăng ký lại các viên gạch mới vào hệ thống va chạm
            for (Brick brick : newBricks) {
                collisionSystem.register(brick);
            }
        }

        // 4. Reset lại bóng
        entities.spawnNewBallAtPaddle();
    }

    public void update(float deltaTime) {
        skillManager.update(deltaTime);
        entities.updateAll(deltaTime);

        collisionProcessor.processCollisions();

        entities.getManaOrbs().stream()
                .filter(orb -> !orb.isAlive()) // already picked
                .forEach(orb -> scoreSystem.addMana(orb.getManaAmount()));

        entities.cleanupDestroyed();

        if (entities.noBallsRemaining()) {
            scoreSystem.loseLife();
            // LOGIC MẤT MẠNG VÀ GAME OVER ĐÃ ĐƯỢC ĐƠN GIẢN HÓA
            if (scoreSystem.isGameOver()) {
                // nếu thua ko phát âm thanh
                return;
            } else {
                // PHÁT ÂM THANH KHI MẤT MẠNG NHƯNG CÒN MẠNG
                SoundManager.play(SoundManager.LOST_HEALTH);
                entities.spawnNewBallAtPaddle();
            }
        }

        skillEffectManager.update(deltaTime);
    }

    public void render(Graphics2D g2) {
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
}
