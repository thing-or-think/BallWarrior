package game.collision;

import core.Constants;
import entity.Ball;
import entity.Entity;
import utils.Vector2D;

public class CollisionUtils {

    // ========================
    // 1. HÀM TIỆN ÍCH
    // ========================

    protected static boolean isBetween(float a, float b, float target) {
        return target >= Math.min(a, b) - Constants.COLLISION_EPSILON &&
                target <= Math.max(a, b) + Constants.COLLISION_EPSILON;
    }

    protected static float clamp(float v, float a, float b) {
        if (a > b) { float t = a; a = b; b = t; }
        if (v < a) return a;
        if (v > b) return b;
        return v;
    }

    protected static Vector2D reflect(Vector2D v, Vector2D normal) {
        Vector2D n = normal.normalized();
        float dp = v.dot(n);
        return v.subtracted(n.multiplied(2f * dp));
    }

    protected static Vector2D nearestPointOnAABB(Vector2D p, Entity aabb) {
        float nx = clamp(p.x, aabb.getX(), aabb.getX() + aabb.getWidth());
        float ny = clamp(p.y, aabb.getY(), aabb.getY() + aabb.getHeight());
        return new Vector2D(nx, ny);
    }

    protected static Vector2D[][] getSweptAabbEdges(Ball ball, Entity entity) {
        float r = (ball == null) ? 0f : ball.getRadius();

        Vector2D topLeft = new Vector2D(entity.getX() - r, entity.getY() - r);
        Vector2D topRight = new Vector2D(entity.getX() + entity.getWidth() + r, entity.getY() - r);
        Vector2D bottomLeft = new Vector2D(entity.getX() - r, entity.getY() + entity.getHeight() + r);
        Vector2D bottomRight = new Vector2D(entity.getX() + entity.getWidth() + r, entity.getY() + entity.getHeight() + r);

        return new Vector2D[][] {
                {topLeft, topRight},       // cạnh trên
                {topRight, bottomRight},   // cạnh phải
                {bottomRight, bottomLeft}, // cạnh dưới
                {bottomLeft, topLeft}      // cạnh trái
        };
    }

    // ========================
    // 2. VA CHẠM AABB
    // ========================

    protected static boolean checkCollision(Entity a, Entity b) {
        float axCurr = a.getX();
        float ayCurr = a.getY();
        float axPrev = a.getPreviousPosition().x;
        float ayPrev = a.getPreviousPosition().y;
        float aw = a.getWidth();
        float ah = a.getHeight();

        float bx = b.getX();
        float by = b.getY();
        float bw = b.getWidth();
        float bh = b.getHeight();

        boolean collCurr = axCurr < bx + bw + Constants.COLLISION_EPSILON &&
                axCurr + aw > bx - Constants.COLLISION_EPSILON &&
                ayCurr < by + bh + Constants.COLLISION_EPSILON &&
                ayCurr + ah > by - Constants.COLLISION_EPSILON;

        boolean collPrev = axPrev < bx + bw + Constants.COLLISION_EPSILON &&
                axPrev + aw > bx - Constants.COLLISION_EPSILON &&
                ayPrev < by + bh + Constants.COLLISION_EPSILON &&
                ayPrev + ah > by - Constants.COLLISION_EPSILON;

        return collCurr && collPrev;
    }

    // ========================
    // 3. GIAO ĐIỂM ĐOẠN THẲNG
    // ========================

    protected static Vector2D getLineIntersection(Vector2D A, Vector2D B, Vector2D C, Vector2D D) {
        float a1 = B.y - A.y;
        float b1 = A.x - B.x;
        float c1 = a1 * A.x + b1 * A.y;

        float a2 = D.y - C.y;
        float b2 = C.x - D.x;
        float c2 = a2 * C.x + b2 * C.y;

        float det = a1 * b2 - a2 * b1;
        if (Math.abs(det) < Constants.COLLISION_EPSILON) return null;

        float x = (b2 * c1 - b1 * c2) / det;
        float y = (a1 * c2 - a2 * c1) / det;

        if (isBetween(A.x, B.x, x) && isBetween(A.y, B.y, y) &&
                isBetween(C.x, D.x, x) && isBetween(C.y, D.y, y)) {
            return new Vector2D(x, y);
        }
        return null;
    }

    // ========================
    // 4. GIAO ĐIỂM HÌNH TRÒN
    // ========================

    protected static Vector2D[] circleLineIntersection(Vector2D C, float r, Vector2D P1, Vector2D P2) {
        Vector2D d = P2.subtracted(P1);
        Vector2D f = P1.subtracted(C);

        float a = d.dot(d);
        float b = 2 * f.dot(d);
        float c = f.dot(f) - r * r;

        float discriminant = b * b - 4 * a * c;

        if (discriminant < -Constants.COLLISION_EPSILON) {
            // Không có nghiệm thực
            return new Vector2D[0];
        }
        if (Math.abs(discriminant) <= Constants.COLLISION_EPSILON) {
            // Tiếp xúc (1 nghiệm)
            float t = -b / (2 * a);
            return new Vector2D[]{ P1.added(d.multiplied(t)) };
        }

        // Hai nghiệm
        float sqrtD = (float) Math.sqrt(discriminant);
        float t1 = (-b - sqrtD) / (2 * a);
        float t2 = (-b + sqrtD) / (2 * a);

        return new Vector2D[]{
                P1.added(d.multiplied(t1)),
                P1.added(d.multiplied(t2))
        };
    }


    protected static Vector2D[] circleSegmentIntersection(Vector2D C, float r, Vector2D P1, Vector2D P2) {
        Vector2D[] points = circleLineIntersection(C, r, P1, P2);
        Vector2D d = P2.subtracted(P1);
        int count = 0;
        Vector2D[] result = new Vector2D[2];

        for (Vector2D p : points) {
            if (p == null) continue;
            float tX = d.x != 0 ? (p.x - P1.x) / d.x : Float.NaN;
            float tY = d.y != 0 ? (p.y - P1.y) / d.y : Float.NaN;
            float t = Float.isNaN(tX) ? tY : Float.isNaN(tY) ? tX : (tX + tY) * 0.5f;

            if (isBetween(0f, 1f, t)) result[count++] = p;
        }

        if (count == 0) return new Vector2D[0];
        if (count == 1) return new Vector2D[]{ result[0] };
        return new Vector2D[]{ result[0], result[1] };
    }
}
