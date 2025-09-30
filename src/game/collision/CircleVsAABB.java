package game.collision;

import core.Constants;
import entity.Ball;
import entity.Entity;
import utils.MathUtils;
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
        Vector2D startCenter = ball.getPreviousPosition().added(new Vector2D(ball.getRadius(), ball.getRadius())); // tâm bóng ở frame trước
        Vector2D endCenter = ball.getPosition().added(new Vector2D(ball.getRadius(), ball.getRadius()));           // tâm bóng ở frame hiện tại
        Vector2D movement = endCenter.subtracted(startCenter);  // vector dịch chuyển trong frame
        float radius = ball.getRadius();

        // Mở rộng AABB theo bán kính bóng
        float minX = box.getX() - radius;
        float minY = box.getY() - radius;
        float maxX = box.getX() + box.getWidth() + radius;
        float maxY = box.getY() + box.getHeight() + radius;

        Vector2D[][] expandedEdges = getSweptAabbEdges(ball, box);
        Vector2D[][] originalEdges = getSweptAabbEdges(null, box);
        CollisionResult bestResult = null;

        for (int edgeIndex = 0; edgeIndex < expandedEdges.length; edgeIndex++) {
            Vector2D intersection = MathUtils.getLineIntersection(
                    startCenter, endCenter,
                    expandedEdges[edgeIndex][0], expandedEdges[edgeIndex][1]
            );

            if (intersection != null) {
                if (isBetween(originalEdges[edgeIndex][0].x, originalEdges[edgeIndex][1].x, intersection.x) ||
                        isBetween(originalEdges[edgeIndex][0].y, originalEdges[edgeIndex][1].y, intersection.y)) {

                    Vector2D offset = intersection.subtracted(startCenter);
                    float time = offset.length() / movement.length();

                    if (bestResult == null || time < bestResult.getTime() - Constants.COLLISION_EPSILON) {
                        Vector2D hitPoint = new Vector2D(
                                clamp(intersection.x, originalEdges[edgeIndex][0].x, originalEdges[edgeIndex][1].x),
                                clamp(intersection.y, originalEdges[edgeIndex][0].y, originalEdges[edgeIndex][1].y)
                        );
                        Vector2D normal = originalEdges[edgeIndex][1].subtracted(originalEdges[edgeIndex][0]).normalLeft();
                        Vector2D reflectedVelocity = reflect(ball.getVelocity(), normal);

                        bestResult = new CollisionResult(box, hitPoint, normal, time, reflectedVelocity);
                    }
                } else {
                    CollisionResult cornerResult = checkCorners(ball, startCenter, endCenter, originalEdges[edgeIndex], box);
                    if (bestResult == null || cornerResult.getTime() < bestResult.getTime() - Constants.COLLISION_EPSILON) {
                        bestResult = cornerResult;
                    }
                }
            }
        }
        return bestResult;
    }

    // Kiểm tra va chạm với 4 góc (point vs moving point with radius)
    private static CollisionResult checkCorners(Ball ball, Vector2D startCenter, Vector2D endCenter, Vector2D[] edgePoints, Entity box) {
        CollisionResult bestResult = null;
        Vector2D movement = endCenter.subtracted(startCenter);

        for (int i = 0; i < edgePoints.length; i++) {
            Vector2D[] intersections = MathUtils.circleSegmentIntersection(edgePoints[i], ball.getRadius(), startCenter, endCenter);

            for (int j = 0; j < intersections.length; j++) {
                Vector2D offset = intersections[j].subtracted(startCenter);
                float time = offset.length() / movement.length();

                if (bestResult == null || time < bestResult.getTime() - Constants.COLLISION_EPSILON) {
                    Vector2D hitPoint = edgePoints[i];
                    Vector2D normal = intersections[j].subtracted(hitPoint);
                    Vector2D reflectedVelocity = reflect(ball.getVelocity(), normal);

                    bestResult = new CollisionResult(box, hitPoint, normal, time, reflectedVelocity);
                }
            }
        }
        return bestResult;
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

    private static Vector2D[][] getSweptAabbEdges(Ball ball, Entity entity) {
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

    private static boolean isBetween(float a, float b, float target) {
        return target >= Math.min(a, b) - Constants.COLLISION_EPSILON &&
                target <= Math.max(a, b) + Constants.COLLISION_EPSILON;
    }

    public static void handleBallInsideEntity(Ball ball, Entity entity) {

        if (!checkCollision(ball, entity)) {
            return;
        }

        Vector2D entityPrev = entity.getPreviousPosition();
        Vector2D entityCurr = entity.getPosition();

        if (entityPrev.x < entityCurr.x) {
            ball.setPosition(entity.getPosition().x + entity.getWidth(), ball.getY());
        } else if (entityPrev.x > entityCurr.x) {
            ball.setPosition(entity.getPosition().x - ball.getWidth(), ball.getY());
        }

        ball.clampPosition();
    }

    public static boolean checkCollision(Entity a, Entity b) {
        return a.getX() < b.getX() + b.getWidth() + Constants.COLLISION_EPSILON &&
                a.getX() + a.getWidth() > b.getX() - Constants.COLLISION_EPSILON &&
                a.getY() < b.getY() + b.getHeight() + Constants.COLLISION_EPSILON &&
                a.getY() + a.getHeight() > b.getY() - Constants.COLLISION_EPSILON;
    }
}
