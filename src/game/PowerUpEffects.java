package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PowerUpEffects {

    private static class Explosion {
        float x, y;
        float radius;
        float alpha;

        Explosion(float x, float y) {
            this.x = x;
            this.y = y;
            this.radius = 20;
            this.alpha = 1.0f;
        }

        void update(float dt) {
            radius += 200 * dt;   // nở ra
            alpha -= 0.8f * dt;   // mờ dần
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 100, 0, (int)(alpha * 255)));
            g2d.fillOval((int)(x - radius), (int)(y - radius),
                    (int)(radius * 2), (int)(radius * 2));
        }

        boolean isFinished() {
            return alpha <= 0;
        }
    }

    // Danh sách hiệu ứng đang chạy
    private final List<Explosion> explosions = new ArrayList<>();

    public void spawnExplosion(float x, float y) {
        explosions.add(new Explosion(x, y));
    }

    public void update(float dt) {
        Iterator<Explosion> it = explosions.iterator();
        while (it.hasNext()) {
            Explosion e = it.next();
            e.update(dt);
            if (e.isFinished()) {
                it.remove();
            }
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (Explosion e : explosions) {
            e.draw(g2d);
        }
    }
}
