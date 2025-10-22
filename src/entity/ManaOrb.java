package entity;

import core.ResourceLoader;
import utils.Constants;

import java.awt.*;

public class ManaOrb extends Entity {
    private int manaAmount;
    private int frameCount = 4;       // có 4 frame trong sprite sheet
    private int currentFrame = 0;
    private int frameDelay = 10;      // số lần update giữa 2 frame
    private int frameTimer = 0;

    public ManaOrb(float x, float y, int manaAmount) {
        super(x, y, 16, 16);
        this.manaAmount = manaAmount;
        this.img = ResourceLoader.loadImage("assets/images/manaOrb.png");
    }

    @Override
    public void update() {
        position.y += 1;

        // animation
        frameTimer++;
        if (frameTimer >= frameDelay) {
            frameTimer = 0;
            currentFrame = (currentFrame + 1) % frameCount;
        }

        if (position.y > Constants.HEIGHT) {
            this.setAlive(false);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (img != null) {
            // Tính phần cắt cho frame hiện tại
            int sx = currentFrame * width;
            int sy = 0;

            g.drawImage(
                    img,
                    (int) position.x, (int) position.y,               // toạ độ vẽ
                    (int) position.x + width, (int) position.y + height, // kích thước 16x16
                    sx, sy, sx + width, sy + height,                 // phần cắt từ sprite sheet
                    null
            );
        } else {
            // fallback nếu ảnh không load được
            g.setColor(Color.CYAN);
            g.fillOval((int) position.x, (int) position.y, width, height);
        }
    }

    public int getManaAmount() {
        return manaAmount;
    }
}
