package game;

import game.effect.SkillEffectManager;
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
    private PowerUpSystem powerUpSystem;
    private PowerUpEffects powerUpEffects;
    private SkillEffectManager skillEffectManager;

    public GameScene(InputHandler input) {
        this.input = input;
        init();
    }

    /**
     * Khởi tạo màn chơi:
     * - Paddle ở giữa đáy
     * - Bóng trên paddle
     * - Reset điểm/mạng
     * - Load bricks từ file level
     */
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

        // Khởi tạo các hệ thống mới và HUD
        powerUpEffects = new PowerUpEffects();

        // Truyền CollisionSystem vào PowerUpSystem để nó có thể đăng ký Shield
        collisionSystem = new CollisionSystem(paddle);
        skillEffectManager = new SkillEffectManager();
        powerUpSystem = new PowerUpSystem(input, paddle, balls, bricks, powerUpEffects, scoreSystem, collisionSystem, skillEffectManager);

        hud = new HUD(scoreSystem);

        // Đăng ký entity có thể va chạm
        collisionSystem.register(paddle);
        for (Brick brick : bricks) {
            collisionSystem.register(brick);
        }
        resetBall();
    }

    /**
     * Đặt bóng trở lại trên paddle (dùng khi mất mạng).
     */
    private void resetBall() {
        Ball ball = new Ball(
                paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                paddle.getY() - Constants.BALL_SIZE - 1
        );
        balls.clear();
        balls.add(ball);
        scoreSystem.resetCombo();
        powerUpSystem.deactivateFireBall();
    }

    /**
     * Cập nhật logic game mỗi frame:
     * - Paddle, Ball
     * - Va chạm ball với colliders
     * - Mất mạng khi bóng rơi xuống đáy
     */
    public void update(float deltaTime) {
        paddle.update();

        // Cập nhật PowerUpSystem
        powerUpSystem.update(deltaTime);

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

            // Xử lý va chạm bóng với tường
            handleWallCollision(ball);

            // Xử lý va chạm bóng với paddle. Tắt FireBall nếu có va chạm.
            handlePaddleCollision(ball);

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
                            powerUpSystem.deactivateFireBall();
                        }
                        scoreSystem.resetCombo();
                    }
                } else if (entity instanceof Paddle) {
                    if (collisionSystem.resolveCollision(ball, result)) {
                        if (ball.isFireBall()) {
                            powerUpSystem.deactivateFireBall();
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
                    powerUpSystem.deactivateFireBall();
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

    /**
     * Xử lý va chạm của FireBall với các viên gạch.
     * Logic này được tách riêng biệt.
     */
//    private void handleFireBallCollision() {
//        Iterator<Brick> brickIterator = bricks.iterator();
//        while (brickIterator.hasNext()) {
//            Brick brick = brickIterator.next();
//
//            if (!brick.isDestroyed()) {
//                for (Ball ball : balls) {
//                    if (ball.isFireBall() && CircleVsAABB.intersect(ball, brick) != null) {
//                        brick.hit(brick.getMaxHealth()); // Phá hủy gạch
//                        scoreSystem.addScore(brick.getScoreValue());
//                        scoreSystem.increaseCombo(0.5f);
//                        brickIterator.remove(); // Xóa gạch an toàn
//                        collisionSystem.unregister(brick);
//                        break;
//                    }
//                }
//            }
//        }
//    }

    /**
     * Vẽ toàn bộ scene:
     * - Paddle, Ball, Bricks
     * - HUD (score + lives + combo)
     */
    public void render(Graphics g) {
        paddle.draw(g);

        skillEffectManager.draw((Graphics2D) g);

        // Vẽ tất cả bóng
        for (Ball ball : balls) {
            powerUpEffects.drawFireBallEffect(g, ball);
            ball.draw(g);
        }

        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(g);
            }
        }

        // Vẽ shield
        powerUpEffects.drawShield(g, powerUpSystem.getShield());

        hud.render((Graphics2D) g);
    }

    private void handleWallCollision(Ball ball) {
        // Cần thêm logic xử lý va chạm với tường tại đây.
        // Ví dụ: ball.checkWallCollision();
    }

    private void handlePaddleCollision(Ball ball) {
        // Cần thêm logic xử lý va chạm với paddle tại đây.
        // Ví dụ: ball.checkPaddleCollision(paddle);
    }

    /**
     * Kiểm tra game over (hết mạng).
     */
    public boolean isGameOver() {
        return scoreSystem.isGameOver();
    }
}
