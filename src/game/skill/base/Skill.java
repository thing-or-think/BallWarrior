package game.skill.base;

public abstract class Skill {
    protected String name;
    protected boolean isReady = true;

    public Skill(String name) {
        this.name = name;
    }

    public abstract void activate();

    public abstract void update(float deltaTime); // ✅ thêm void

    public boolean isReady() {
        return isReady;
    }

    public String getName() {
        return name;
    }
}
