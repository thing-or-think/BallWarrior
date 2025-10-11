package entity;

import utils.Constants;
import core.ResourceLoader;
import core.InputHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Paddle extends Entity {
    private InputHandler input;
    public static BufferedImage equippedPaddleImage = null;
    public static Color equippedPaddleColor = null;

    public static void loadEquippedAssets() {
        int equippedPaddleId = ResourceLoader.getEquippedPaddleId("docs/paddles.txt");
        List<Skins> allBalls = ResourceLoader.loadSkins("docs/paddles.txt");
        Skins equippedSkin = null;
        for (Skins skin : allBalls) {
            if (skin.getId() == equippedPaddleId) {
                equippedSkin = skin;
                break;
            }
        }
        if (equippedSkin != null) {
            if (equippedSkin.getImg() != null) {
                equippedPaddleImage = equippedSkin.getImg();
                equippedPaddleColor = null;
            } else {
                equippedPaddleColor = equippedSkin.getColor();
                equippedPaddleImage = null;
            }
        } else {
            equippedPaddleImage = null;
            equippedPaddleColor = Color.RED;
        }
        System.out.println("✅ Assets Paddle Equipped Loaded to static field.");
    }

    public Paddle(float x, float y, InputHandler input) {
        super(x, y, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT);
        this.input = input;
        this.img = equippedPaddleImage;
    }

    @Override
    public void clampPosition() {
        if (position.x < 0) position.x = 0;
        if (position.x + width > Constants.WIDTH) position.x = Constants.WIDTH - width;
    }


    @Override
    public void update() {
        velocity.x = 0;

        if (input.isLeftPressed()) {
            velocity.x = -Constants.PADDLE_SPEED;
        } else if (input.isRightPressed()) {
            velocity.x = Constants.PADDLE_SPEED;
        }

        previousPosition.set(position.x, position.y);
        position.add(velocity);

        clampPosition();
    }

    @Override
    public void draw(Graphics g) {
        if (img!=null) {
            g.drawImage(img,(int)position.x,(int)position.y,Constants.PADDLE_WIDTH,Constants.PADDLE_HEIGHT,null);
        }else {
            g.setColor(equippedPaddleColor);
            g.fillRect((int) position.x, (int) position.y, width, height);
        }
    }
}
