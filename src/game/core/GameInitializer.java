package game.core;

import core.InputHandler;
import entity.*;
import game.collision.CollisionSystem;
import game.skill.base.ActiveSkill;
import game.skill.effect.SkillEffectManager;
import game.skill.SkillManager;
import game.ScoreSystem;
import game.LevelBuilder;
import game.LevelData;
import game.LevelManager;
import utils.Constants;

import java.util.List;

/**
 * Giúp khởi tạo các thành phần game và inject dependency.
 */
public class GameInitializer {

    private EntityManager entityManager;
    private CollisionSystem collisionSystem;
    private SkillManager skillManager;
    private SkillEffectManager skillEffectManager;
    private ScoreSystem scoreSystem;
    private OrbSpawner orbSpawner;

    public void initialize(InputHandler input) {
        // Score
        scoreSystem = new ScoreSystem();
        ActiveSkill.setScoreSystem(scoreSystem);

        // Entity manager & paddle
        entityManager = new EntityManager();
        Paddle paddle = new Paddle(Constants.GAME_PANEL_WIDTH / 2 - Constants.PADDLE_WIDTH / 2,
                Constants.GAME_PANEL_HEIGHT - Constants.PADDLE_HEIGHT - 30, input);
        entityManager.setPaddle(paddle);

        // Level
        LevelManager levelManager = new LevelManager();
        levelManager.load("assets/levels/level1.json");
        LevelData level = levelManager.getCurrentLevel();
        List<Brick> bricks = LevelBuilder.buildBricks(level);
        entityManager.setBricks(bricks);

        // Collision system
        collisionSystem = new CollisionSystem(paddle);
        collisionSystem.register(paddle);
        for (Brick b : bricks) collisionSystem.register(b);

        // Skills
        skillEffectManager = new SkillEffectManager();
        skillManager = new SkillManager(
                input,
                scoreSystem,
                collisionSystem,
                skillEffectManager,
                entityManager
        );

        // Orb spawner (mặc định)
        orbSpawner = new DefaultOrbSpawner(0.3, 10);

        // initially spawn a ball
        entityManager.spawnNewBallAtPaddle();
    }

    /* ---------- getters to inject into GameWorld ---------- */
    public EntityManager getEntityManager() { return entityManager; }
    public CollisionSystem getCollisionSystem() { return collisionSystem; }
    public SkillManager getSkillManager() { return skillManager; }
    public SkillEffectManager getSkillEffectManager() { return skillEffectManager; }
    public ScoreSystem getScoreSystem() { return scoreSystem; }
    public OrbSpawner getOrbSpawner() { return orbSpawner; }
}
