package game.skill;

import core.InputHandler;
import game.ScoreSystem;
import game.core.EntityManager;
import game.skill.base.ActiveSkill;
import game.skill.effect.SkillEffectManager;
import game.skill.active.ExplosionSkill;
import game.skill.active.MultiBallSkill;
import game.skill.active.ShieldSkill;
import game.collision.CollisionSystem;

import java.util.ArrayList;
import java.util.List;

public class SkillManager {

    private InputHandler input;
    private List<ActiveSkill> activeSkills;

    public SkillManager(InputHandler input,
                        ScoreSystem scoreSystem,
                        CollisionSystem collisionSystem,
                        SkillEffectManager skillEffectManager,
                        EntityManager entityManager) {
        this.input = input;
        activeSkills = new ArrayList<>();
        activeSkills.add(new MultiBallSkill(entityManager.getBalls(), skillEffectManager));
        activeSkills.add(new ExplosionSkill(entityManager.getBalls(), entityManager.getBricks(), skillEffectManager));
        activeSkills.add(new ShieldSkill(collisionSystem, entityManager.getShield()));
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
