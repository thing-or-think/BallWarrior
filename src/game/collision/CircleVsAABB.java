package game.collision;

import utils.Constants;
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

    public static CollisionResult intersect(Ball ball, Entity box) {
        if (ball.getVelocity().x == 0 && ball.getVelocity().y == 0) {
            return null;
        }


        float radius = ball.getRadius();
        Vector2D expandedMin = new Vector2D(box.getX() - radius, box.getY() - radius);
        Vector2D expandedMax = new Vector2D(box.getX() + box.getWidth() + radius, box.getY() + box.getHeight() + radius);

        Vector2D startPoint = ball.getPreviousPosition().added(new Vector2D(radius, radius));

        Vector2D velocity = ball.getVelocity();

        float t_near_x, t_far_x, t_near_y, t_far_y;

        if (velocity.x == 0) {
            if (startPoint.x < expandedMin.x || startPoint.x > expandedMax.x) {
                return null;
            }
            t_near_x = -Float.MAX_VALUE;
            t_far_x = Float.MAX_VALUE;
        } else {
            t_near_x = (expandedMin.x - startPoint.x) / velocity.x;
            t_far_x = (expandedMax.x - startPoint.x) / velocity.x;
        }

        if (velocity.y == 0) {
            if (startPoint.y < expandedMin.y || startPoint.y > expandedMax.y) {
                return null;
            }
            t_near_y = -Float.MAX_VALUE;
            t_far_y = Float.MAX_VALUE;
        } else {
            t_near_y = (expandedMin.y - startPoint.y) / velocity.y;
            t_far_y = (expandedMax.y - startPoint.y) / velocity.y;
        }

        if (t_near_x > t_far_x) { float temp = t_near_x; t_near_x = t_far_x; t_far_x = temp; }
        if (t_near_y > t_far_y) { float temp = t_near_y; t_near_y = t_far_y; t_far_y = temp; }

        float t_entry = Math.max(t_near_x, t_near_y);
        float t_exit = Math.min(t_far_x, t_far_y);

        if (t_entry >= t_exit || t_entry > 1.0f || t_entry < 0.0f) {
            return null;
        }

        Vector2D normal;
        if (t_near_x > t_near_y) {
            normal = new Vector2D(-Math.signum(velocity.x), 0);
        } else {
            normal = new Vector2D(0, -Math.signum(velocity.y));
        }

        Vector2D hitPoint = startPoint.added(velocity.multiplied(t_entry));
        Vector2D reflectedVelocity = CollisionUtils.reflect(ball.getVelocity(), normal);

        return new CollisionResult(box, hitPoint, normal, t_entry, reflectedVelocity);
    }

    // Kiểm tra va chạm với 4 góc (point vs moving point with radius)
    private static CollisionResult checkCorners(Ball ball, Vector2D startCenter, Vector2D endCenter, Vector2D[] edgePoints, Entity box) {
        CollisionResult bestResult = null;
        Vector2D movement = endCenter.subtracted(startCenter);

        for (int i = 0; i < edgePoints.length; i++) {
            Vector2D[] intersections = CollisionUtils.circleLineIntersection(edgePoints[i], ball.getRadius(), startCenter, endCenter);

            for (int j = 0; j < intersections.length; j++) {
                Vector2D offset = intersections[j].subtracted(startCenter);
                float time = offset.length() / movement.length();

                if (bestResult == null || time < bestResult.getTime() - Constants.COLLISION_EPSILON) {
                    Vector2D hitPoint = edgePoints[i];
                    Vector2D normal = intersections[j].subtracted(hitPoint).normalized();
                    float angle = intersections[j].subtracted(edgePoints[1 - i]).angle(normal);
                    if (Math.abs(angle) <= Math.PI / 2 + Constants.COLLISION_EPSILON) {
                        continue;
                    }
                    Vector2D reflectedVelocity = CollisionUtils.reflect(ball.getVelocity(), normal);
                    bestResult = new CollisionResult(box, hitPoint, normal, time, reflectedVelocity);
                }
            }
        }
        return bestResult;
    }


    // Tính điểm gần nhất trên AABB tới một điểm





    public static boolean handleBallInsideEntity(Ball ball, Entity entity) {

        if (!CollisionUtils.checkCollision(ball, entity)) {
            return false;
        }

        Vector2D entityPrev = entity.getPreviousPosition();
        Vector2D entityCurr = entity.getPosition();

        if (entityPrev.x < entityCurr.x) {
            ball.setPosition(entity.getPosition().x + entity.getWidth() + 0.01f, ball.getY());
            return true;
        } else if (entityPrev.x > entityCurr.x) {
            ball.setPosition(entity.getPosition().x - ball.getWidth() - 0.01f, ball.getY());
            return true;
        }
        ball.clampPosition();
        return false;
    }

}
