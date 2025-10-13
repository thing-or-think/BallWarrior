package game.skill.effect;

import entity.Shield;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SkillEffectManager {
    private final List<ExplosionEffect> explosions;
    private Shield shield;

    public SkillEffectManager() {
        explosions = new ArrayList<>();
    }

    public void spawnExplosion(float x, float y, float maxRadius) {
        explosions.add(new ExplosionEffect(x, y, maxRadius));
    }

    public void update(float deltaTime) {
        Iterator<ExplosionEffect> it = explosions.iterator();
        while (it.hasNext()) {
            ExplosionEffect e = it.next();
            e.update(deltaTime);
            if (e.isFinished()) it.remove();
        }
    }

    public void setShield(Shield shield) {
        this.shield = shield;
    }

    public void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (ExplosionEffect e : explosions) {
            e.draw(g);
        }
        if (shield != null) {
            shield.draw(g);
        }
    }

}
