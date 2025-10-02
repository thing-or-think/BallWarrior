package game;

import core.InputHandler;
import core.Constants;
import entity.Ball;
import entity.Paddle;
import entity.Shield;
import entity.Brick;

import java.util.ArrayList;
import java.util.List;

public class PowerUpSystem {

    private InputHandler input;
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;

    private Shield shield; // chỉ tạo khi bấm Q

    public PowerUpSystem(InputHandler input, Paddle paddle, List<Ball> balls, List<Brick> bricks) {
        this.input = input;
        this.paddle = paddle;
        this.balls = balls;
        this.bricks = bricks;
    }

    public void update(float deltaTime) {
        handleInput();
        if (shield != null) {
            shield.update(deltaTime);
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
        for (Ball b : balls) {
            Ball copy = new Ball(b.getX(), b.getY());
            copy.setVelocity(b.getVelocity().x, b.getVelocity().y);
            newBalls.add(copy);
        }
        balls.addAll(newBalls);
        System.out.println("Multi Ball activated! Total balls: " + balls.size());
    }

    private void activateExplosion() {
        float explosionRadius = 100f; // bán kính vụ nổ

        for (Ball b : balls) {
            float explosionX = b.getX();
            float explosionY = b.getY();

            System.out.println("Explosion triggered at ball (" + explosionX + "," + explosionY + ")");

            for (Brick brick : bricks) {
                if (!brick.isDestroyed()) {
                    float dx = brick.getX() - explosionX;
                    float dy = brick.getY() - explosionY;
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
        for (Ball b : balls) {
            b.setFireBall(true);
        }
        System.out.println("Fire Ball activated!");
    }

    public Shield getShield() {
        return shield;
    }
}
