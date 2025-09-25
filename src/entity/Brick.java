package entity;

import java.awt.Color;
import java.awt.Graphics;
import utils.Vector2D;

public class Brick extends Entity {

    private int health;   // Độ bền (số lần cần đánh để vỡ)
    private Color color;  // Màu gạch

    public Brick(float x, float y, int width, int height, int health, Color color) {
        super(x, y, width, height);
        this.health = health;
        this.color = color;
        this.velocity = new Vector2D(0, 0); // gạch đứng yên
    }

    @Override
    public void update() {
        // Brick không di chuyển, có thể để trống
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect((int) position.x, (int) position.y, width, height);

        // Vẽ viền
        g.setColor(Color.BLACK);
        g.drawRect((int) position.x, (int) position.y, width, height);
    }

    public void hit() {
        health--;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }
}
