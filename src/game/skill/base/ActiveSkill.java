package game.skill.base;

import utils.Constants;

import java.awt.image.BufferedImage;

public abstract class ActiveSkill extends Skill {
    protected float cooldownTime;
    protected float cooldownTimer;
    protected int manaCost;
    protected int key;

    public ActiveSkill(String name,
                       BufferedImage icon,
                       float cooldown,
                       int key) {
        super(name, icon);
        this.cooldownTime = cooldown;
        this.cooldownTimer = cooldown;
        this.isReady = true;
        this.key = key;
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

    public void tryActivate(int pressedKey) {
        if (pressedKey == key) {
            activate();
        }
    }

    @Override
    protected void activate() {
        if (isReady) {
            boolean success = performAction();
            if (success) {
                isReady = false;
                cooldownTimer = cooldownTime;
            }
        }
    }

    protected abstract boolean performAction();

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
