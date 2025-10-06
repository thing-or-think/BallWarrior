package game;

import core.InputHandler;
import entity.Ball;
import entity.Brick;
import entity.Paddle;
import entity.Shield;
import utils.Vector2D;
import utils.Constants;
import game.collision.CollisionSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class PowerUpSystem {

    private InputHandler input;
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private PowerUpEffects powerUpEffects;
    private ScoreSystem score;
    private CollisionSystem collisionSystem;

    // Các biến trạng thái của power-up
    private boolean fireBallActive = false;
    private boolean shieldActive = false;

    private Shield shield;

    public PowerUpSystem(InputHandler input, Paddle paddle, List<Ball> balls, List<Brick> bricks, PowerUpEffects powerUpEffects, ScoreSystem score, CollisionSystem collisionSystem) {
        this.input = input;
        this.paddle = paddle;
        this.balls = balls;
        this.bricks = bricks;
        this.powerUpEffects = powerUpEffects;
        this.score = score;
        this.collisionSystem = collisionSystem;
    }

    public void update(float deltaTime) {
        handleInput();
        if (shield != null) {
            shield.update(deltaTime);
            // Hủy đăng ký shield khi hết thời gian
            if (!shield.isActive()) {
                collisionSystem.unregister(shield);
                shield = null; // Xóa tham chiếu
                System.out.println("Shield deactivated!");
            }
        }
    }

    private void handleInput() {
        // Q - Shield
        if (input.isQPressed() && (shield == null || !shield.isActive())) {
            float shieldWidth = Constants.WIDTH;
            float shieldHeight = 10;
            float x = 0;
            float y = Constants.HEIGHT - 40;
            float duration = 5f; // giây

            shield = new Shield(x, y, (int) shieldWidth, (int) shieldHeight, duration);
            shield.activate();
            collisionSystem.register(shield);
            System.out.println("Shield activated!");
        }

        // W - Multi Ball
        if (input.isWPressed()) {
            activateMultiBall();
        }

        // E - Explosion
        if (input.isEPressed()) {
            activateExplosion();
        }

        // R - Fire Ball
        if (input.isRPressed()) {
            activateFireBall();
        }
    }

    private void activateMultiBall() {
        List<Ball> newBalls = new ArrayList<>();
        float angle90Deg = (float) Math.toRadians(90);

        for (Ball b : new ArrayList<>(balls)) {
            Vector2D originalVelocity = b.getVelocity();

            // Bỏ qua nếu bóng đang dính hoặc tốc độ quá chậm
            if (b.isStuck() || originalVelocity.length() < 1.0f) {
                continue;
            }

            // Tạo một bản sao của vector vận tốc gốc để không làm thay đổi nó
            Vector2D newVelocity1 = new Vector2D(originalVelocity.x, originalVelocity.y);
            Vector2D newVelocity2 = new Vector2D(originalVelocity.x, originalVelocity.y);

            // Bóng mới thứ nhất: Xoay vector vận tốc sang trái
            newVelocity1.rotate(-angle90Deg);

            Ball newBall1 = new Ball(b.getX(), b.getY());
            newBall1.setVelocity(newVelocity1.x, newVelocity1.y);
            newBall1.setStuck(false);
            newBalls.add(newBall1);

            // Bóng mới thứ hai: Xoay vector vận tốc sang phải
            newVelocity2.rotate(angle90Deg);

            Ball newBall2 = new Ball(b.getX(), b.getY());
            newBall2.setVelocity(newVelocity2.x, newVelocity2.y);
            newBall2.setStuck(false);
            newBalls.add(newBall2);

            balls.remove(b);
        }

        balls.addAll(newBalls);
        System.out.println("Multi Ball activated! Total balls: " + balls.size());
    }

    private void activateExplosion() {
        float explosionRadius = 100f;

        for (Ball b : balls) {
            float explosionX = b.getX() + b.getWidth() / 2f;
            float explosionY = b.getY() + b.getHeight() / 2f;

            powerUpEffects.spawnExplosion(explosionX, explosionY, explosionRadius);

            System.out.println("Explosion triggered at ball (" + explosionX + "," + explosionY + ")");

            for (Brick brick : bricks) {
                if (!brick.isDestroyed()) {
                    float dx = (brick.getX() + brick.getWidth()/2) - explosionX;
                    float dy = (brick.getY() + brick.getHeight()/2) - explosionY;
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);

                    if (distance <= explosionRadius) {
                        int damage = getDamageByDistance(distance, explosionRadius);
                        brick.hit(damage);
                        System.out.println("Brick at (" + brick.getX() + "," + brick.getY() + ") took " + damage + " damage");
                    }
                }
            }
        }
    }

    private int getDamageByDistance(float distance, float radius) {
        if (distance <= radius / 3f) return 5;   // gần tâm
        if (distance <= (2f * radius) / 3f) return 3; // trung bình
        return 1; // xa
    }

    private void activateFireBall() {
        if (!fireBallActive) {
            for (Ball b : balls) {
                b.setFireBall(true);
            }
            fireBallActive = true;
        }
    }

    public void deactivateFireBall() {
        if (fireBallActive) {
            for (Ball b : balls) {
                b.setFireBall(false);
            }
            fireBallActive = false;
        }
    }

    public Shield getShield() {
        return shield;
    }
}
