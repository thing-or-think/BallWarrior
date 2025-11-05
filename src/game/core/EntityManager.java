package game.core;

import core.AudioService;
import entity.*;
import entity.Ball;
import utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Quản lý các entity (paddle, balls, bricks, mana orbs).
 * Giữ API tối thiểu cần cho GameWorld / CollisionProcessor.
 */
public class EntityManager {
    private Paddle paddle;
    private final List<Ball> balls = new ArrayList<>();
    private final List<Brick> bricks = new ArrayList<>();
    private final List<ManaOrb> manaOrbs = new ArrayList<>();
    private final Shield shield;

    public EntityManager() {
        shield = new Shield(0,
                Constants.GAME_PANEL_HEIGHT - 40,
                Constants.GAME_PANEL_WIDTH,
                10,
                3f);
    }

    /* ---------- Init / inject ---------- */
    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public void setBricks(List<Brick> bricks) {
        this.bricks.clear();
        if (bricks != null) this.bricks.addAll(bricks);
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public List<ManaOrb> getManaOrbs() {
        return manaOrbs;
    }

    public Shield getShield() {
        return shield;
    }

    /* ---------- Ball helpers ---------- */
    public void spawnNewBallAtPaddle() {
        if (paddle == null) return;
        Ball ball = new Ball(
                paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                paddle.getY() - Constants.BALL_SIZE - 1
        );
        balls.clear();
        balls.add(ball);
    }

    public boolean noBallsRemaining() {
        return balls.isEmpty();
    }

    public void addBall(Ball ball) {
        if (ball != null) balls.add(ball);
    }

    /* ---------- ManaOrb ---------- */
    public void addManaOrb(ManaOrb orb) {
        if (orb != null) manaOrbs.add(orb);
    }

    /* ---------- Update & Render ---------- */
    public void updateAll(float deltaTime) {
        if (paddle != null) paddle.update();

        // update balls
        Iterator<Ball> bit = balls.iterator();
        while (bit.hasNext()) {
            Ball ball = bit.next();
            if (ball.isStuck() && paddle != null) {
                ball.reset(paddle.getX() + Constants.PADDLE_WIDTH / 2 - Constants.BALL_SIZE / 2,
                        paddle.getY() - Constants.BALL_SIZE);
                if (paddle.getInput().isKeyJustPressed(KeyEvent.VK_SPACE)) { ball.launch(); }
            } else {
                ball.update();
                if (!ball.isAlive()) {
                    bit.remove();
                    continue;
                }
            }
        }

        Iterator<ManaOrb> mit = manaOrbs.iterator();
        while (mit.hasNext()) {
            ManaOrb orb = mit.next();
            orb.update();

            if (!orb.isAlive()) {
                mit.remove();
                continue;
            }

            if (paddle != null && orb.intersects(paddle)) {
                AudioService.playSound("orb_collect.wav");
                orb.setAlive(false);
            }
        }
    }

    public void cleanupDestroyed() {
        bricks.removeIf(brick -> !brick.isAlive());
        balls.removeIf(ball -> !ball.isAlive());
        manaOrbs.removeIf(orb -> !orb.isAlive());
    }

    public void render(Graphics2D g2) {
        if (paddle != null) paddle.draw(g2);
        for (Brick b : bricks) if (!b.isDestroyed()) b.draw(g2);
        for (Ball ball : balls) ball.draw(g2);
        for (ManaOrb orb : manaOrbs) orb.draw(g2);
        shield.draw(g2);
    }
}
