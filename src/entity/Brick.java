package entity;

import utils.Vector2D;
import core.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Brick extends Entity {

    public enum Type {
        BEDROCK, COBBLESTONE, GOLD, DIAMOND
    }

    private int health;   // Độ bền (số lần cần đánh để vỡ)
    private int scoreValue;
    private Type type;
    private BufferedImage texture;
    private final int initialHealth;


    public Brick(float x, float y, int width, int height, Type type) {
        super(x, y, width, height);
        this.type = type;
        this.velocity = new Vector2D(0, 0); // gạch đứng yên

        switch (type) {
            case BEDROCK:
                this.health = Integer.MAX_VALUE; // không bao giờ vỡ
                this.scoreValue = 0;
                this.texture = ResourceLoader.loadImage("images/bedrock.png");
                break;
            case COBBLESTONE:
                this.health = 1;
                this.scoreValue = 100;
                this.texture = ResourceLoader.loadImage("images/cobblestone.png");
                break;
            case GOLD:
                this.health = 5;
                this.scoreValue = 500;
                this.texture = ResourceLoader.loadImage("images/gold.png");
                break;
            case DIAMOND:
                this.health = 10;
                this.scoreValue = 10000;
                this.texture = ResourceLoader.loadImage("images/diamond.png");
                break;
        }
        this.initialHealth = this.health;
    }

    @Override
    public void update() {
        // Brick không di chuyển, có thể để trống
    }

    @Override
    public void draw(Graphics g) {
        if (!isDestroyed()) {
            g.drawImage(texture, (int) position.x, (int) position.y, width, height, null);
        }
    }

    // hit có damage (skill bom, fireball, v.v.)
    public void hit(int damage) {
        if (type != Type.BEDROCK) { // Bedrock không thể phá
            health -= damage;
        }
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public Type getType() {
        return type;
    }

    public int getMaxHealth() {
        return initialHealth;
    }
}
