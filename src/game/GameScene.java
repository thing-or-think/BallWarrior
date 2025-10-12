package game;

import game.effect.SkillEffectManager;
import game.skill.SkillManager;
import utils.Constants;
import core.InputHandler;
import entity.Ball;
import entity.Brick;
import entity.Paddle;
import entity.Shield;
import entity.Entity;
import ui.HUD;
import game.collision.CollisionSystem;
import game.collision.CollisionResult;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScene {

    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private InputHandler input;
    private HUD hud;

    private ScoreSystem scoreSystem;
    private LevelManager levelManager;
    private CollisionSystem collisionSystem;
    private SkillManager skillManager;
    private SkillEffectManager skillEffectManager;

    public GameScene(InputHandler input) {
        this.input = input;
        init();
    }

    private void init() {
        paddle = new Paddle(
                Constants.WIDTH / 2 - Constants.PADDLE_WIDTH / 2,
                Constants.HEIGHT - Constants.PADDLE_HEIGHT - 30,
                input
        );

        scoreSystem = new ScoreSystem();
        // Bóng ngay trên paddle
        balls = new ArrayList<>();

        scoreSystem = new ScoreSystem();
        levelManager = new LevelManager();
        bricks = levelManager.load("assets/levels/level1.txt");


        collisionSystem = new CollisionSystem(paddle);
        skillEffectManager = new SkillEffectManager();
        skillManager = new SkillManager(input, paddle, balls, bricks, scoreSystem, collisionSystem, skillEffectManager);

        hud = new HUD(scoreSystem);

        // Đăng ký entity có thể va chạm
        collisionSystem.register(paddle);
        for (Brick brick : bricks) {
            collisionSystem.register(brick);
        }
        resetBall();
    }


    public void update(float deltaTime) {
        paddle.update();

        // Cập nhật PowerUpSystem
        skillManager.update(deltaTime);

        // Xử lý va chạm đặc biệt của FireBall trước va chạm thông thường
       // handleFireBallCollision();

        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();

            // Cập nhật vị trí bóng
            if (ball.isStuck()) {
                ball.reset(
                        paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                        paddle.getY() - Constants.BALL_SIZE
                );
                if (input.isSpacePressed()) {
                    ball.launch();
                }
            } else {
                ball.update();
            }



            // Tìm và xử lý va chạm gần nhất(thông thường)
            CollisionResult result = collisionSystem.findNearestCollision(ball);
            if (result != null) {
                // Đây là điểm mấu chốt: Cấu trúc if-else if phải độc lập.
                Entity entity = result.getEntity();

                if (entity instanceof Brick brick) {
                    if (collisionSystem.resolveCollision(ball, result)) {
                        if (brick.isDestroyed()) {
                            scoreSystem.addScore(brick.getScoreValue());
                            scoreSystem.increaseCombo(0.5f);
                            collisionSystem.unregister(brick);
                        }
                    }
                } else if (entity instanceof Shield) {
                    if (collisionSystem.resolveCollision(ball, result)) {
                        if (ball.isFireBall()) {
                            skillManager.deactivateFireBall();
                        }
                        scoreSystem.resetCombo();
                    }
                } else if (entity instanceof Paddle) {
                    if (collisionSystem.resolveCollision(ball, result)) {
                        if (ball.isFireBall()) {
                            skillManager.deactivateFireBall();
                        }
                        scoreSystem.resetCombo();
                    }
                }
            }
        }

        // Xóa gạch đã bị phá hủy
        bricks.removeIf(Brick::isDestroyed);

        // Xóa các quả bóng rơi khỏi màn hình
        balls.removeIf(ball -> {
            if (ball.getY() > Constants.HEIGHT) {
                if (ball.isFireBall()) {
                    skillManager.deactivateFireBall();
                }
                return true;
            }
            return false;
        });

        // Nếu hết bóng → mất mạng và reset
        if (balls.isEmpty()) {
            scoreSystem.loseLife();
            resetBall();
        }

        skillEffectManager.update(deltaTime);
    }

    public void render(Graphics2D g) {
        paddle.draw(g);

        skillEffectManager.draw((Graphics2D) g);

        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(g);
            }
        }

        for (Ball ball : balls) {
            ball.draw(g);
        }

        hud.render((Graphics2D) g);
    }

    private void resetBall() {
        Ball ball = new Ball(
                paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                paddle.getY() - Constants.BALL_SIZE - 1
        );
        balls.clear();
        balls.add(ball);
        scoreSystem.resetCombo();
        skillManager.deactivateFireBall();
    }

}
