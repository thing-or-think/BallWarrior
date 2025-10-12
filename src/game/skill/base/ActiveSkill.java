package game.skill.base;

public abstract class ActiveSkill extends Skill {
    protected float cooldown;
    protected float cooldownTimer;

    public ActiveSkill(String name, float cooldown) {
        super(name);
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
