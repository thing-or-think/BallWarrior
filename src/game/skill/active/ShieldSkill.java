package game.skill.active;

import core.ResourceLoader;
import entity.Shield;
import game.collision.CollisionSystem;
import game.skill.effect.SkillEffectManager;
import game.skill.base.ActiveSkill;
import utils.Constants;

public class ShieldSkill extends ActiveSkill {
    private static final String path = "assets/images/skills/Shield_ball.png";

    private CollisionSystem collisionSystem;
    private Shield shield;
    private SkillEffectManager skillEffectManager;

    public ShieldSkill(CollisionSystem collisionSystem, SkillEffectManager skillEffectManager) {
        super("SHIELD",
                ResourceLoader.loadImage(path),
                0f);
        this.collisionSystem = collisionSystem;
        this.skillEffectManager = skillEffectManager;
    }

    @Override
    protected void performAction() {
        if (shield != null && shield.isActive()) {
            return; // Nếu đang có shield hoạt động, không tạo thêm
        }

        float shieldWidth = Constants.WIDTH;
        float shieldHeight = 10;
        float x = 0;
        float y = Constants.HEIGHT - 40;
        float duration = 5f; // giây

        shield = new Shield(x, y, (int) shieldWidth, (int) shieldHeight, duration);
        shield.activate();
        collisionSystem.register(shield);
        skillEffectManager.setShield(shield);

        System.out.println("Shield activated!");
    }

    public Shield getShield() {
        return shield;
    }
}
