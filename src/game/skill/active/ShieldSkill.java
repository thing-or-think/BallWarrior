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

    public ShieldSkill(CollisionSystem collisionSystem,
                       SkillEffectManager skillEffectManager,
                       Shield shield) {
        super("SHIELD",
                ResourceLoader.loadImage(path),
                4.5f);
        this.collisionSystem = collisionSystem;
        this.skillEffectManager = skillEffectManager;
        this.shield = shield;
    }

    @Override
    protected boolean performAction() {
        if (shield != null && shield.isAlive()) {
            return false; // Nếu đang có shield hoạt động, không tạo thêm
        }

        shield.setAlive(true);
        collisionSystem.register(shield);
        skillEffectManager.setShield(shield);

        System.out.println("Shield activated!");
        return true;
    }
}
