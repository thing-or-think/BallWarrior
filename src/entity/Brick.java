package entity;

import core.ResourceLoader;

import java.awt.Color;
import java.awt.Graphics;
import utils.Vector2D;

public class Brick extends Entity {

    private int health;   // Độ bền (số lần cần đánh để vỡ)
    private Color color;  // Màu gạch
    private final int initialHealth; // máu gốc

    public Brick(float x, float y, int width, int height, int health, Color color) {
        super(x, y, width, height);
        this.health = health;
        this.initialHealth = this.health;
        this.color = color;
        this.velocity = new Vector2D(0, 0); // gạch đứng yên
        this.img = ResourceLoader.loadImg("BallWarrior-master/assets/images/red.png");
    }

    @Override
    public void update() {
        // Brick không di chuyển, có thể để trống
    }

    @Override
    public void draw(Graphics g) {
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
        //if (type != Type.BEDROCK) { // Bedrock không thể phá
            health -= damage;
        //}
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public int getMaxHealth() {
        return initialHealth;
    }

    public int getScoreValue(){
        return 100;
    }
}
