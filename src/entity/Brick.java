package entity;

import core.ResourceLoader;

import java.awt.*;

import utils.Vector2D;

import java.awt.image.BufferedImage;

public class Brick extends Entity {

    // loại brick
    public enum Type {
        BEDROCK, COBBLESTONE, GOLD, DIAMOND
    }

    private int health;   // Độ bền (số lần cần đánh để vỡ)
    private int scoreValue;
    private Type type;
    private final int initialHealth; // máu gốc

    // Ảnh crack overlay
    private BufferedImage crackOverlay;
    private static final BufferedImage[] crackStages = new BufferedImage[10]; // Minecraft có 10 cấp

    static {
        // Load tất cả ảnh crack vào bộ nhớ
        for (int i = 0; i < 10; i++) {
            crackStages[i] = ResourceLoader.loadImg("assets/images/Brick/cracks/destroy" + i + ".png");
        }
    }

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
        this.crackOverlay = null;
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

        // Vẽ crack overlay nếu có
        if (crackOverlay != null && type != Type.BEDROCK && !isDestroyed()) {
            g.drawImage(crackOverlay, (int) position.x, (int) position.y, width, height, null);
        }
        // Vẽ viền
        g.setColor(Color.BLACK);
        g.drawRect((int) position.x, (int) position.y, width, height);
    }

    // hit có damage (skill bom, fireball, v.v.)
    public void hit(int damage) {
        if (type == Type.BEDROCK) return;

        health -= damage;
        if (health < 0) health = 0;

        // Tính mức độ nứt dựa trên tỉ lệ máu còn lại
        float percent = 1f - ((float) health / initialHealth);
        int level = Math.min(9, Math.max(0, (int) (percent * 10))); // 0–9

        // Gán ảnh crack tương ứng
        crackOverlay = crackStages[level];
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
