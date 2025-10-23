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
        activeSkills = new ArrayList<>();
        activeSkills.add(new MultiBallSkill(entityManager.getBalls()));
        activeSkills.add(new ExplosionSkill(entityManager.getBalls(), entityManager.getBricks(), skillEffectManager));
        activeSkills.add(new ShieldSkill(collisionSystem, skillEffectManager, entityManager.getShield()));
    }

    public void update(float deltaTime) {
        for (ActiveSkill skill : activeSkills) {
            skill.tryActivate(input.getLastKeyPressed());
            skill.update(deltaTime);
        }
    }

    public List<ActiveSkill> getActiveSkills() {
        return activeSkills;
    }
}
