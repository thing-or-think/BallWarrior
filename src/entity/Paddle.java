package entity;

import data.SkinData;
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
    private static SkinData skinData;

    public Paddle(float x, float y, InputHandler input) {
        super(x, y, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT);
        this.input = input;
        this.img = equippedPaddleImage;
    }

    public static void setSkin(SkinData skinData) {
        if (skinData.equals(Paddle.skinData)) {
            return;
        } else {
            Paddle.skinData = skinData;
            loadDisplay();
        }
    }

    private static void loadDisplay() {
        if (skinData == null || skinData.getDisplay() == null) return;

        String type = skinData.getDisplay().getType();
        String value = skinData.getDisplay().getValue();

        if ("color".equalsIgnoreCase(type)) {
            try {
                equippedPaddleColor = Color.decode(value);
                if (equippedPaddleColor == null) {
                    equippedPaddleColor = Color.WHITE;
                }
            } catch (Exception e) {
                equippedPaddleColor = Color.WHITE;
            }
        } else if ("image".equalsIgnoreCase(type)) {
            equippedPaddleImage = ResourceLoader.loadImage(value);
        }
    }

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
    public void draw(Graphics2D g) {
        String type = skinData.getDisplay().getType();

        if ("color".equalsIgnoreCase(type)) {
            g.setColor(equippedPaddleColor);
            g.fillRect((int) position.x, (int) position.y, width, height);
        }else {
            g.drawImage(img,(int)position.x,(int)position.y,Constants.PADDLE_WIDTH,Constants.PADDLE_HEIGHT,null);

        }
    }

    public InputHandler getInput() {
        return input;
    }
}
