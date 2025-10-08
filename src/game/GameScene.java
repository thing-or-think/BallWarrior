package game;

import core.InputHandler;
import core.SceneManager;
import entity.Ball;
import entity.Brick;
import entity.Paddle;
import game.collision.CollisionResult;
import game.collision.CollisionSystem;
import ui.HUD;
import ui.base.Scene;
import utils.Constants;

import java.awt.*;
import java.util.List;

public class GameScene extends Scene {

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;

    private ScoreSystem scoreSystem;
    private LevelManager levelManager;
    private CollisionSystem collisionSystem;

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

        resetBall();

        scoreSystem = new ScoreSystem();
        levelManager = new LevelManager();
        levelManager.load("assets/levels/level1.json");
        LevelData level = levelManager.getCurrentLevel();

        buildBricks(level);

        collisionSystem = new CollisionSystem(paddle);
        collisionSystem.register(paddle);
        for (Brick brick : bricks) {
            collisionSystem.register(brick);
        }

    }

    @Override
    protected void update() {

        // Nhấn ESC để bật/tắt pause
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ESCAPE)) {
            sceneManager.goToPause();
        }

        paddle.update();
        ball.update();

        CollisionResult result = collisionSystem.findNearestCollision(ball);
        if (collisionSystem.resolveCollision(ball, result)) {
            if (result.getEntity() instanceof Brick brick) {
                if (brick.isDestroyed()) {
                    scoreSystem.addScore(100);
                    collisionSystem.unregister(brick);
                }
            }
        }

        if (ball.getY() > Constants.HEIGHT) {
            scoreSystem.loseLife();
            resetBall();
        }

    }

    @Override
    protected void render(Graphics2D g2) {
        drawBackground(g2);

        paddle.draw(g2);
        ball.draw(g2);

        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(g2);
            }
        }

        HUD.render(g2, scoreSystem);

    }

    private void resetBall() {
        ball = new Ball(
                paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                paddle.getY() - Constants.BALL_SIZE - 1
        );
    }

    public boolean isGameOver() {
        return scoreSystem.isGameOver();
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
