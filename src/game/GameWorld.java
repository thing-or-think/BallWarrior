package game;

import core.InputHandler;
import data.SkinData;
import entity.*;
import entity.Ball;
import game.core.*;
import game.collision.CollisionSystem;
import game.skill.effect.SkillEffectManager;
import game.skill.SkillManager;

import java.awt.*;

public class GameWorld {
    private final EntityManager entities;
    private final CollisionProcessor collisionProcessor;
    private final SkillManager skillManager;
    private final SkillEffectManager skillEffectManager;
    private final ScoreSystem scoreSystem;

    public GameWorld(InputHandler input) {
        GameInitializer init = new GameInitializer();
        init.initialize(input);

        this.entities = init.getEntityManager();
        CollisionSystem collisionSystem = init.getCollisionSystem();
        this.skillManager = init.getSkillManager();
        this.skillEffectManager = init.getSkillEffectManager();
        this.scoreSystem = init.getScoreSystem();

        // collision processor wired with orbSpawner
        this.collisionProcessor = new CollisionProcessor(collisionSystem, init.getOrbSpawner(), entities, scoreSystem);
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
            entities.spawnNewBallAtPaddle();
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
