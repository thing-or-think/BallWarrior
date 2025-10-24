package game.skill.effect;


import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SkillEffectManager {
    private final List<ExplosionEffect> explosions;
    private final List<FireBallVisualEffect> fireBallEffects;

    public SkillEffectManager() {
        explosions = new ArrayList<>();
        fireBallEffects = new ArrayList<>();
    }

    public void spawnExplosion(float x, float y, float maxRadius) {
        explosions.add(new ExplosionEffect(x, y, maxRadius));
    }

    // Phương thức để thêm hiệu ứng Fire Ball
    public void addFireBallVisualEffect(FireBallVisualEffect effect) {
        // ⭐ SỬA LỖI: Dùng getBall() thay vì truy cập trực tiếp ball
        boolean exists = fireBallEffects.stream()
                // So sánh đối tượng Ball bằng == (vì chúng là các instance duy nhất)
                .anyMatch(e -> e.getBall() == effect.getBall());

        if (!exists) {
            fireBallEffects.add(effect);
        }
    }

    public void update(float deltaTime) {
        Iterator<ExplosionEffect> it = explosions.iterator();
        while (it.hasNext()) {
            ExplosionEffect e = it.next();
            e.update(deltaTime);
            if (e.isFinished()) it.remove();
        }

        // THÊM: Cập nhật và xóa Fire Ball Effects
        Iterator<FireBallVisualEffect> fit = fireBallEffects.iterator();
        while (fit.hasNext()) {
            FireBallVisualEffect f = fit.next();
            f.update(deltaTime);
            if (!f.isActive()) fit.remove();
        }
    }

    public void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (ExplosionEffect e : explosions) {
            e.draw(g);
        }
        // Vẽ Fire Ball Effects TRƯỚC các entity và vụ nổ
        for (FireBallVisualEffect f : fireBallEffects) {
            f.draw(g);
        }
    }

}
