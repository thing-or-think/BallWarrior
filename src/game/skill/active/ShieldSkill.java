package game.skill.active;

import core.ResourceLoader;
import entity.Shield;
import game.collision.CollisionSystem;
import game.skill.effect.SkillEffectManager;
import game.skill.base.ActiveSkill;
import utils.Constants;

import java.awt.event.KeyEvent;

public class ShieldSkill extends ActiveSkill {
    private static final String path = "assets/images/skills/Shield_ball.png";

    private CollisionSystem collisionSystem;
    private Shield shield;

    public ShieldSkill(CollisionSystem collisionSystem,
                       Shield shield) {
        super("SHIELD",
                ResourceLoader.loadImage(path),
                4.5f,
                KeyEvent.VK_Q,
                10,
                3f);
        this.collisionSystem = collisionSystem;
        this.shield = shield;
    }

    @Override
    protected boolean performAction() {
        if (shield != null && shield.isAlive()) {
            return false; // Nếu đang có shield hoạt động, không tạo thêm
        }

        shield.setAlive(true);
        collisionSystem.register(shield);

        System.out.println("Shield activated!");
        return true;
    }

    @Override
    protected void onActivate() {
        super.onActivate();
    }

    @Override
    protected void onDeactivate() {
        super.onDeactivate();
    }
}
