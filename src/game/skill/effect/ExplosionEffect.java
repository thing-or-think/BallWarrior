package game.skill.effect;

import java.awt.*;

public class ExplosionEffect {
    private float x, y;
    private float radius;
    private float alpha;
    private final float maxRadius;
    private final float expandSpeed = 300f;
    private final float fadeSpeed = 1.0f;

    public ExplosionEffect(float x, float y, float maxRadius) {
        this.x = x;
        this.y = y;
        this.radius = 1f;
        this.alpha = 1.0f;
        this.maxRadius = maxRadius;
    }

    public void update(float dt) {
        radius = Math.min(radius + expandSpeed * dt, maxRadius);
        alpha -= fadeSpeed * dt;
    }

    public void draw(Graphics2D g) {
        if (alpha <= 0) return;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Spark layer
        float sparkRadius = radius * 0.8f;
        g.setColor(new Color(255, 255, 0, Math.max(0, Math.min(255, (int)(alpha * 255)))));
        g.fillOval((int)(x - sparkRadius), (int)(y - sparkRadius),
                (int)(sparkRadius * 2), (int)(sparkRadius * 2));

        // Main explosion layer
        g.setColor(new Color(255, 100, 0, Math.max(0, Math.min(255, (int)(alpha * 255)))));
        g.fillOval((int)(x - radius), (int)(y - radius),
                (int)(radius * 2), (int)(radius * 2));
    }

    public boolean isFinished() {
        return alpha <= 0;
    }

    /**
     * Trả về lượng sát thương dựa trên khoảng cách đến tâm nổ
     */
    private int getDamageByDistance(float distance, float radius) {
        if (distance <= radius / 3f) return 5;       // gần tâm
        if (distance <= (2f * radius) / 3f) return 3; // trung bình
        return 1;                                    // xa
    }
}
