package game.skill;

import core.InputHandler;
import entity.Ball;
import entity.Brick;
import entity.Paddle;
import entity.Shield;
import game.ScoreSystem;
import game.core.EntityManager;
import game.skill.base.ActiveSkill;
import game.skill.effect.SkillEffectManager;
import game.skill.active.ExplosionSkill;
import game.skill.active.MultiBallSkill;
import game.skill.active.ShieldSkill;
import game.collision.CollisionSystem;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SkillManager {

    private InputHandler input;
    private CollisionSystem collisionSystem;
    private ScoreSystem scoreSystem;
    private final EntityManager entityManager;

    private MultiBallSkill multiBallSkill;
    private ExplosionSkill explosionSkill;
    private ShieldSkill shieldSkill;

    private List<ActiveSkill> activeSkills;

    // Các biến trạng thái của power-up
    private boolean fireBallActive = false;

    private Shield shield;

    public SkillManager(InputHandler input,
                        ScoreSystem scoreSystem,
                        CollisionSystem collisionSystem,
                        SkillEffectManager skillEffectManager,
                        EntityManager entityManager) {
        this.input = input;
        this.collisionSystem = collisionSystem;
        this.entityManager = entityManager;
        this.scoreSystem = scoreSystem;
        this.multiBallSkill = new MultiBallSkill(entityManager.getBalls());
        this.explosionSkill = new ExplosionSkill(entityManager.getBalls(), entityManager.getBricks(), skillEffectManager);
        this.shieldSkill = new ShieldSkill(collisionSystem, skillEffectManager, entityManager.getShield());
        activeSkills = new ArrayList<>();
        activeSkills.add(multiBallSkill);
        activeSkills.add(explosionSkill);
        activeSkills.add(shieldSkill);
    }

    public void update(float deltaTime) {
        handleInput();
        for (ActiveSkill skill : activeSkills) {
            skill.update(deltaTime);
        }
    }

    private void handleInput() {
        // Q - Shield
        if (input.isKeyJustPressed(KeyEvent.VK_Q)) {
            shieldSkill.activate();
        }

        // W - Multi Ball
        if (input.isKeyJustPressed(KeyEvent.VK_W)) {
            multiBallSkill.activate();
        }

        // E - Explosion
        if (input.isKeyJustPressed(KeyEvent.VK_E)) {
            explosionSkill.activate();
        }
        // R - Fire Ball
        if (input.isKeyJustPressed(KeyEvent.VK_R)) {
            System.out.println("Fire Ball activated!");
        }

    }

    public List<ActiveSkill> getActiveSkills() {
        return activeSkills;
    }
}
