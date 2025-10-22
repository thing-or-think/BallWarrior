package game;

import core.InputHandler;
import data.SkinData;
import entity.*;
import game.collision.*;
import game.skill.effect.SkillEffectManager;
import game.skill.SkillManager;
import utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameWorld {
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private List<ManaOrb> manaOrbs = new ArrayList<>();

    private ScoreSystem scoreSystem;
    private LevelManager levelManager;
    private CollisionSystem collisionSystem;
    private SkillManager skillManager;
    private SkillEffectManager skillEffectManager;

    public GameWorld(InputHandler input) {
        init(input);
    }

    private void init(InputHandler input) {
        paddle = new Paddle(Constants.WIDTH / 2 - Constants.PADDLE_WIDTH / 2,
                Constants.HEIGHT - Constants.PADDLE_HEIGHT - 30, input);
        balls = new ArrayList<>();
        scoreSystem = new ScoreSystem();
        levelManager = new LevelManager();
        levelManager.load("assets/levels/level1.json");

        LevelData level = levelManager.getCurrentLevel();
        bricks = LevelBuilder.buildBricks(level);

        collisionSystem = new CollisionSystem(paddle);
        skillEffectManager = new SkillEffectManager();
        skillManager = new SkillManager(input, paddle, balls, bricks, scoreSystem, collisionSystem, skillEffectManager);

        collisionSystem.register(paddle);
        for (Brick brick : bricks) collisionSystem.register(brick);

        resetBall();
    }

    public void update(float deltaTime) {
        skillManager.update(deltaTime);
        paddle.update();

        Iterator<Ball> it = balls.iterator();
        while (it.hasNext()) {
            Ball ball = it.next();

            if (ball.isStuck()) {
                ball.reset(paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                        paddle.getY() - Constants.BALL_SIZE);
                if (paddle.getInput().isKeyJustPressed(KeyEvent.VK_SPACE)) {
                    ball.launch();
                }
            } else {
                ball.update();
            }

            CollisionResult result = collisionSystem.findNearestCollision(ball);
            if (result != null) handleCollision(ball, result);
        }

        for (ManaOrb manaOrb : manaOrbs) {
            manaOrb.update();
        }

        bricks.removeIf(Brick::isDestroyed);
        balls.removeIf(ball -> ball.getY() > Constants.HEIGHT);
        manaOrbs.removeIf(orb -> !orb.isAlive());

        if (balls.isEmpty()) {
            scoreSystem.loseLife();
            resetBall();
        }

        skillEffectManager.update(deltaTime);
    }

    private void handleCollision(Ball ball, CollisionResult result) {
        Entity entity = result.getEntity();

        if (entity instanceof Brick brick) {
            if (collisionSystem.resolveCollision(ball, result)) {
                if (brick.isDestroyed()) {
                    if (Math.random() < 0.3) {
                        manaOrbs.add(new ManaOrb(brick.getX(), brick.getY(), 10));
                    }
                    scoreSystem.addScore(brick.getScoreValue());
                    scoreSystem.increaseCombo(0.5f);
                    collisionSystem.unregister(brick);
                }
            }
        } else if (entity instanceof Shield || entity instanceof Paddle) {
            if (collisionSystem.resolveCollision(ball, result)) {
                scoreSystem.resetCombo();
            }
        }
    }

    public void render(Graphics2D g2) {
        paddle.draw(g2);
        skillEffectManager.draw(g2);
        for (Brick brick : bricks) if (!brick.isDestroyed()) brick.draw(g2);
        for (Ball ball : balls) ball.draw(g2);
        for (ManaOrb manaOrb : manaOrbs) manaOrb.draw(g2);
    }

    private void resetBall() {
        Ball ball = new Ball(
                paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                paddle.getY() - Constants.BALL_SIZE - 1
        );
        balls.clear();
        balls.add(ball);
        scoreSystem.resetCombo();
    }

    public ScoreSystem getScoreSystem() {
        return scoreSystem;
    }

    public void forceUpdateGameAssets(SkinData ballSkin, SkinData paddleSkin) {
        // Tải lại Skin vào cache static
        Ball.setSkin(ballSkin);
        Paddle.setSkin(paddleSkin);

        // Cập nhật đối tượng đang tồn tại
        for (Ball ball : balls) {
            if (ball != null) {
                ball.setImg(Ball.equippedBallImage);
                ball.setColor(Ball.equippedBallColor);

            }
        }
        if (paddle != null) {
            paddle.setImg(Paddle.equippedPaddleImage);
            paddle.setColor(Paddle.equippedPaddleColor);
        }
    }

    public boolean isGameOver() {
        return scoreSystem.isGameOver();
    }
}
