package entity;

import core.ResourceLoader;

import java.awt.*;

import utils.Vector2D;

public class Brick extends Entity {

    // loại brick
    public enum Type {
        BEDROCK, COBBLESTONE, GOLD, DIAMOND
    }

    private int health;   // Độ bền (số lần cần đánh để vỡ)
    private int scoreValue;
    private Type type;
    private final int initialHealth; // máu gốc

    public Brick(float x, float y, int width, int height, Type type) {
        super(x, y, width, height);
        this.type = type;
        this.velocity = new Vector2D(0, 0); // gạch đứng yên

        switch (type) {
            case BEDROCK:
                this.health = Integer.MAX_VALUE; // không bao giờ vỡ
                this.scoreValue = 0;
                this.img = ResourceLoader.loadImg("assets/images/Brick/bedrock.png");
                break;
            case COBBLESTONE:
                this.health = 1;
                this.scoreValue = 100;
                this.img = ResourceLoader.loadImg("assets/images/Brick/cobblestone.png");
                break;
            case GOLD:
                this.health = 5;
                this.scoreValue = 500;
                this.img = ResourceLoader.loadImg("assets/images/Brick/gold.png");
                break;
            case DIAMOND:
                this.health = 10;
                this.scoreValue = 10000;
                this.img = ResourceLoader.loadImg("assets/images/Brick/diamond.png");
                break;
        }

        this.initialHealth = this.health;
    }

    @Override
    public void update() {
        // Brick không di chuyển, có thể để trống
    }

    @Override
    public void draw(Graphics2D g) {
        if (img!=null) {
            g.drawImage(img,(int)position.x,(int)position.y,width,height,null);
        }else {
            g.setColor(color);
            g.fillRect((int) position.x, (int) position.y, width, height);
        }
        // Vẽ viền
        g.setColor(Color.BLACK);
        g.drawRect((int) position.x, (int) position.y, width, height);
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

    public int getMaxHealth() {
        return initialHealth;
    }

    public Type getType() {
        return type;
    }

    public int getScoreValue(){
        return scoreValue;
    }
}
