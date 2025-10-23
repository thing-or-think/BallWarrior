package game.skill.base;

import utils.Constants;

import java.awt.image.BufferedImage;

public abstract class ActiveSkill extends Skill {
    protected float cooldownTime;
    protected float cooldownTimer;

    public ActiveSkill(String name,
                       BufferedImage icon,
                       float cooldown) {
        super(name, icon);
        this.cooldownTime = cooldown;
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
            isReady = false;
            cooldownTimer = cooldownTime;
        }
    }

    protected abstract void performAction();

    public float getCooldownTimer() {
        return cooldownTimer;
    }

    public float getCooldownProgress() {
        if (cooldownTime <= Constants.COLLISION_EPSILON) {
            return 0;
        }
        return cooldownTimer / cooldownTime;
    }
}
