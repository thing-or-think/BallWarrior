package entity;

import core.Constants;
import core.ResourceLoader;
import core.InputHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Paddle extends Entity {

    private InputHandler input;
    private BufferedImage[] sprites = new BufferedImage[2];

    public Paddle(float x, float y, InputHandler input) {
        super(x, y, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT);
        this.input = input;
        sprites[0] = ResourceLoader.loadImg("BallWarrior-master/assets/images/paddle2.png");
        sprites[1] = ResourceLoader.loadImg("BallWarrior-master/assets/images/paddle3.png");
        this.setImg(sprites[0]);
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
            g.setColor(Color.CYAN);
            g.fillRect((int) position.x, (int) position.y, width, height);
        }
    }
    public BufferedImage getSprite (int index) {
        return sprites[index];
    }
}
