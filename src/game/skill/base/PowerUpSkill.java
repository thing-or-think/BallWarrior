package game.skill.base;

public abstract class PowerUpSkill extends Skill {
    protected float duration;
    protected float timer;
    protected boolean active = false;

    public PowerUpSkill(String name, float duration) {
        super(name);
        this.duration = duration;
    }

    @Override
    public void update(float deltaTime) {
        if (active) {
            timer += deltaTime;
            if (timer >= duration) {
                timer = 0;
                active = false;
                isReady = true;
                onDeactivate();
            }
        }
    }

    protected abstract void onDeactivate();
}
