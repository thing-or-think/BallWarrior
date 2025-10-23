package game.skill.base;

import java.awt.image.BufferedImage;

public abstract class Skill {
    protected String name;
    protected boolean isReady = true;
    protected BufferedImage icon;

    public Skill(String name, BufferedImage icon) {
        this.name = name;
        this.icon = icon;
    }

    protected abstract void activate();

    public abstract void update(float deltaTime);

    public boolean isReady() {
        return isReady;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getIcon() {
        return icon;
    }
}
