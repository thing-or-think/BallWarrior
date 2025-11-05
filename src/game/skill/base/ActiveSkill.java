package game.skill.base;

import game.ScoreSystem;
import core.AudioService;
import utils.Constants;

import java.awt.image.BufferedImage;

public abstract class ActiveSkill extends Skill {
    protected float cooldownTime;
    protected float cooldownTimer;
    protected float durationTime;
    protected float durationTimer;
    protected int manaCost;
    protected int key;
    protected boolean isActive;
    protected static ScoreSystem scoreSystem;

    public ActiveSkill(String name,
                       BufferedImage icon,
                       float cooldown,
                       int key,
                       int manaCost) {
        super(name, icon);
        this.cooldownTime = cooldown;
        this.cooldownTimer = cooldown;
        this.isReady = true;
        this.key = key;
        this.manaCost = manaCost;
    }


    public ActiveSkill(String name,
                       BufferedImage icon,
                       float cooldown,
                       int key,
                       int manaCost,
                       float duration) {
        super(name, icon);
        this.cooldownTime = cooldown;
        this.cooldownTimer = 0;
        this.durationTime = duration;
        this.durationTimer = 0;
        this.isReady = true;
        this.key = key;
        this.manaCost = manaCost;
    }

    @Override
    public void update(float deltaTime) {
        if (isActive) {
            durationTimer -= deltaTime;
            if (durationTimer <= 0) {
                durationTimer = 0;
                isActive = false;
                onDeactivate();
            }
        }

        if (!isReady) {
            cooldownTimer -= deltaTime;
            if (cooldownTimer <= 0) {
                cooldownTimer = 0;
                isReady = true;
            }
        }
    }

    public void tryActivate(int pressedKey) {
        if (pressedKey == key && scoreSystem.getMana() >= manaCost) {
            activate();
        }
    }

    @Override
    protected void activate() {
        if (!isReady || scoreSystem == null) {
            return;
        }

        boolean success = performAction();
        if (success) {
            scoreSystem.addMana(-manaCost);
            isReady = false;
            cooldownTimer = cooldownTime;

            //phát âm thanh skill
            playActivationSound();

            if (durationTime > 0) {
                isActive = true;
                durationTimer = durationTime;
                onActivate();
            }
        }
    }

    // PHÁT ÂM THANH KÍCH HOẠT CHUNG
    protected void playActivationSound() {
        // Chỉ phát âm thanh SKILL_ACTIVATE chung
        AudioService.playSound("skill.wav");
    }

    public void forceReset() {
        // 1. Dọn dẹp hiệu ứng nếu đang active
        if (isActive) {
            onDeactivate();
        }

        // 2. Reset trạng thái
        isActive = false;
        isReady = true;
        cooldownTimer = 0;
        durationTimer = 0;

    }

    protected void onActivate() {}

    protected void onDeactivate() {}

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

    public static void setScoreSystem(ScoreSystem scoreSystem) {
        ActiveSkill.scoreSystem = scoreSystem;
    }

    public int getManaCost() {
        return manaCost;
    }
}
