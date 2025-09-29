package game.collision;

import core.Constants;
import entity.Ball;
import entity.Entity;
import utils.Vector2D;

/**
 * Circle (ball) vs AABB continuous collision detection (swept circle).
 *
 * Chiến lược:
 * 1) "Mở rộng" AABB theo bán kính bi: expand the box by radius and treat the ball as a point moving
 *    along segment prev->curr. Dùng phương pháp slab (Liang-Barsky style) để tìm thời điểm vào (tEntry).
 * 2) Nếu không có va chạm với mặt phẳng của AABB, kiểm tra va chạm với các góc (solve quadratic).
 * 3) Xử lý trường hợp không di chuyển (d = 0): nếu đang chồng lên AABB => trả về va chạm t=0.
 *
 * Trả về CollisionResult sớm nhất (nhỏ nhất t trong [0,1]) hoặc null nếu không va chạm.
 */
public class CircleVsAABB {

    public static CollisionResult intersect(Ball ball, Entity aabb) {
        Vector2D p0 = ball.getPreviousPosition(); // vị trí tâm bóng trước frame
        Vector2D p1 = ball.getPosition();         // vị trí tâm bóng hiện tại
        Vector2D d = p1.subtracted(p0);           // vector dịch chuyển trong frame
        float r = ball.getRadius();

        // Nếu không di chuyển nhiều — kiểm tra chồng lấn t=0
        if (d.length() <= Constants.COLLISION_EPSILON) {
            // Kiểm tra khoảng cách từ tâm tới AABB (nearest point)
            Vector2D nearest = nearestPointOnAABB(p0, aabb);
            Vector2D diff = p0.subtracted(nearest);
            float dist2 = diff.dot(diff);
            if (dist2 <= r * r + Constants.COLLISION_EPSILON) {
                Vector2D normal = dist2 > Constants.COLLISION_EPSILON ? diff.normalized() : new Vector2D(0, -1);
                Vector2D contact = nearest;
                Vector2D reflected = reflect(ball.getVelocity(), normal);
                return new CollisionResult(aabb, contact, normal, 0f, reflected);
            }
            return null;
        }

        // Mở rộng AABB theo bán kính r
        float minX = aabb.getX() - r;
        float minY = aabb.getY() - r;
        float maxX = aabb.getX() + aabb.getWidth() + r;
        float maxY = aabb.getY() + aabb.getHeight() + r;

        float tEntry = 0f;
        float tExit = 1f;
        float tEntryX, tExitX, tEntryY, tExitY;

        // X axis
        if (Math.abs(d.x) < Constants.COLLISION_EPSILON) {
            if (p0.x < minX || p0.x > maxX) return checkCorners(ball, p0, d, r, aabb); // không thể đi qua box về trục X
            tEntryX = Float.NEGATIVE_INFINITY;
            tExitX = Float.POSITIVE_INFINITY;
        } else {
            float invDx = 1f / d.x;
            float t1 = (minX - p0.x) * invDx;
            float t2 = (maxX - p0.x) * invDx;
            tEntryX = Math.min(t1, t2);
            tExitX = Math.max(t1, t2);
            tEntry = Math.max(tEntry, tEntryX);
            tExit = Math.min(tExit, tExitX);
        }

        // Y axis
        if (Math.abs(d.y) < Constants.COLLISION_EPSILON) {
            if (p0.y < minY || p0.y > maxY) return checkCorners(ball, p0, d, r, aabb);
            tEntryY = Float.NEGATIVE_INFINITY;
            tExitY = Float.POSITIVE_INFINITY;
        } else {
            float invDy = 1f / d.y;
            float t1 = (minY - p0.y) * invDy;
            float t2 = (maxY - p0.y) * invDy;
            tEntryY = Math.min(t1, t2);
            tExitY = Math.max(t1, t2);
            tEntry = Math.max(tEntry, tEntryY);
            tExit = Math.min(tExit, tExitY);
        }

        // Nếu có khoảng giao
        if (tEntry <= tExit && tEntry >= 0f - Constants.COLLISION_EPSILON && tEntry <= 1f + Constants.COLLISION_EPSILON) {
            float tClamped = clamp(tEntry, 0f, 1f);
            Vector2D collisionCenter = p0.added(d.multiplied(tClamped));

            // Xác định pháp tuyến dựa vào trục nào gây entry (so sánh với tEntryX/tEntryY)
            Vector2D normal;
            if (tEntryX > tEntryY) {
                // va chạm trên trục X
                if (d.x > 0) normal = new Vector2D(-1, 0); else normal = new Vector2D(1, 0);
            } else if (tEntryY > tEntryX) {
                if (d.y > 0) normal = new Vector2D(0, -1); else normal = new Vector2D(0, 1);
            } else {
                // trường hợp entry cùng lúc — ưu tiên trục có trị tuyệt đối lớn hơn
                if (Math.abs(d.x) > Math.abs(d.y)) {
                    if (d.x > 0) normal = new Vector2D(-1, 0); else normal = new Vector2D(1, 0);
                } else {
                    if (d.y > 0) normal = new Vector2D(0, -1); else normal = new Vector2D(0, 1);
                }
            }

            // Tính điểm tiếp xúc trên biên của AABB gốc (không expanded) bằng cách clamp center - normal*r
            Vector2D contactPoint = collisionCenter.subtracted(normal.multiplied(r));

            Vector2D reflected = reflect(ball.getVelocity(), normal);
            return new CollisionResult(aabb, contactPoint, normal, tClamped, reflected);
        }

        // Nếu không tìm thấy bằng slab, kiểm tra va chạm với các góc bằng giải phương trình bậc hai
        return checkCorners(ball, p0, d, r, aabb);
    }

    // Kiểm tra va chạm với 4 góc (point vs moving point with radius)
    private static CollisionResult checkCorners(Ball ball, Vector2D p0, Vector2D d, float r, Entity aabb) {
        Vector2D[] corners = new Vector2D[] {
                new Vector2D(aabb.getX(), aabb.getY()),
                new Vector2D(aabb.getX() + aabb.getWidth(), aabb.getY()),
                new Vector2D(aabb.getX() + aabb.getWidth(), aabb.getY() + aabb.getHeight()),
                new Vector2D(aabb.getX(), aabb.getY() + aabb.getHeight())
        };

        float bestT = Float.MAX_VALUE;
        Vector2D bestContact = null;
        Vector2D bestNormal = null;

        float a = d.dot(d);
        for (Vector2D corner : corners) {
            Vector2D m = p0.subtracted(corner);
            float b = 2f * d.dot(m);
            float c = m.dot(m) - r * r;
            float disc = b * b - 4f * a * c;
            if (disc < 0f) continue;
            float sqrtD = (float) Math.sqrt(disc);
            float t1 = (-b - sqrtD) / (2f * a);
            float t2 = (-b + sqrtD) / (2f * a);
            float t = Float.MAX_VALUE;
            if (t1 >= -Constants.COLLISION_EPSILON && t1 <= 1f + Constants.COLLISION_EPSILON) t = t1;
            else if (t2 >= -Constants.COLLISION_EPSILON && t2 <= 1f + Constants.COLLISION_EPSILON) t = t2;
            if (t < 0f) continue;
            if (t <= bestT) {
                bestT = t;
                Vector2D collisionCenter = p0.added(d.multiplied(t));
                Vector2D normal = collisionCenter.subtracted(corner);
                if (normal.length() > Constants.COLLISION_EPSILON) normal = normal.normalized();
                else normal = new Vector2D(0, -1);
                Vector2D contactPoint = corner; // contact on the corner
                bestContact = contactPoint;
                bestNormal = normal;
            }
        }

        if (bestContact != null) {
            Vector2D reflected = reflect(ball.getVelocity(), bestNormal);
            float tClamped = clamp(bestT, 0f, 1f);
            return new CollisionResult(ball == null ? null : null, bestContact, bestNormal, tClamped, reflected);
        }

        return null;
    }

    // Tính điểm gần nhất trên AABB tới một điểm
    private static Vector2D nearestPointOnAABB(Vector2D p, Entity aabb) {
        float nx = clamp(p.x, aabb.getX(), aabb.getX() + aabb.getWidth());
        float ny = clamp(p.y, aabb.getY(), aabb.getY() + aabb.getHeight());
        return new Vector2D(nx, ny);
    }

    private static float clamp(float v, float a, float b) {
        if (a > b) { float t = a; a = b; b = t; }
        if (v < a) return a;
        if (v > b) return b;
        return v;
    }

    private static Vector2D reflect(Vector2D v, Vector2D normal) {
        Vector2D n = normal.normalized();
        float dp = v.dot(n);
        return v.subtracted(n.multiplied(2f * dp));
    }
}
