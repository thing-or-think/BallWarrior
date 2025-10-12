package game;

import game.effect.SkillEffectManager;
import game.skill.SkillManager;
import utils.Constants;
import utils.Constants;
import core.InputHandler;
import core.SceneManager;
import entity.Ball;
import entity.Brick;
import entity.Paddle;
import entity.Shield;
import entity.Entity;
import game.collision.CollisionResult;
import game.collision.CollisionSystem;
import ui.HUD;
import ui.base.Scene;
import utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScene extends Scene {

    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private HUD hud;

    private ScoreSystem scoreSystem;
    private LevelManager levelManager;
    private CollisionSystem collisionSystem;

    private SkillManager skillManager;
    private SkillEffectManager skillEffectManager;

    private SceneManager sceneManager;

    public GameScene(InputHandler input, SceneManager sceneManager) {
        super("Game", input);
        this.sceneManager = sceneManager;
        initUI();
    }

    @Override
    protected void initUI() {
        paddle = new Paddle(
                Constants.WIDTH / 2 - Constants.PADDLE_WIDTH / 2,
                Constants.HEIGHT - Constants.PADDLE_HEIGHT - 30,
                input
        );

        // Bóng ngay trên paddle
        balls = new ArrayList<>();

        scoreSystem = new ScoreSystem();
        levelManager = new LevelManager();
        levelManager.load("assets/levels/level1.json");
        LevelData level = levelManager.getCurrentLevel();

        buildBricks(level);

        collisionSystem = new CollisionSystem(paddle);
        skillEffectManager = new SkillEffectManager();
        skillManager = new SkillManager(input, paddle, balls, bricks, scoreSystem, collisionSystem, skillEffectManager);

        // Đăng ký entity có thể va chạm
        collisionSystem.register(paddle);
        for (Brick brick : bricks) {
            collisionSystem.register(brick);
        }
        resetBall();

        hud = new HUD(scoreSystem);

    }

    @Override
    protected void update() {

        // Nhấn ESC để bật/tắt pause
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ESCAPE)) {
            sceneManager.goToPause();
        }

        skillManager.update(deltaTime);
        paddle.update();

        Iterator<Ball> ballIterator = balls.iterator();

        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();

            // Cập nhật vị trí bóng
            if (ball.isStuck()) {
                ball.reset(
                        paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                        paddle.getY() - Constants.BALL_SIZE
                );
                if (input.isKeyJustPressed(KeyEvent.VK_SPACE)) {
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

    @Override
    protected void render(Graphics2D g2) {
        drawBackground(g2);

        paddle.draw(g2);
        skillEffectManager.draw(g2);


        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(g2);
            }
        }

        for (Ball ball : balls) {
            ball.draw(g2);
        }

        hud.render(g2);
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

    private void buildBricks(LevelData level) {
        bricks = new java.util.ArrayList<>();

        int brickWidth = Constants.WIDTH / level.cols;
        int brickHeight = 25; // hoặc Constants.BRICK_HEIGHT nếu có

        for (int r = 0; r < level.rows; r++) {
            for (int c = 0; c < level.cols; c++) {
                if (level.brickMap[r][c] == 1) {
                    int x = c * brickWidth;
                    int y = 100 + r * brickHeight; // cách mép trên 100px
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, Color.RED));
                }
            }
        }
    }
}