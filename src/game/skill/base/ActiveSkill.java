package game.skill.base;

import java.awt.image.BufferedImage;

public abstract class ActiveSkill extends Skill {
    protected float cooldown;
    protected float cooldownTimer;

    public ActiveSkill(String name,
                       BufferedImage icon,
                       float cooldown) {
        super(name, icon);
        this.cooldown = cooldown;
        this.cooldownTimer = cooldown;
        this.isReady = true;
    }

    @Override
    public void update(float deltaTime) {
        if (!isReady) {
            cooldownTimer -= deltaTime;
            if (cooldownTimer <= 0) {
                cooldownTimer = 0;
                isReady = true;
            }
        }
    }

    @Override
    public void activate() {
        if (isReady) {
            performAction();
//            isReady = false;
            cooldownTimer = cooldown;
        }
    }

    protected abstract void performAction();
}
