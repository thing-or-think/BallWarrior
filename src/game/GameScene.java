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

    private boolean paused = false;  // <— Thêm biến tạm dừng

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
        bricks = levelManager.load("assets/levels/level1.txt");

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

        // Nếu đang tạm dừng, bỏ qua cập nhật game
        if (paused) return;

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

        // Nếu đang pause — hiển thị overlay mờ
        if (paused) {
            g2.setColor(new Color(0, 0, 0, 150)); // lớp phủ mờ
            g2.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            String text = "PAUSED";
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, (Constants.WIDTH - textWidth) / 2, Constants.HEIGHT / 2);
        }

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
}
