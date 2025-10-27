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
        Vector2D startCenter = ball.getPreviousPosition().added(new Vector2D(ball.getRadius(), ball.getRadius())); // tâm bóng ở frame trước
        Vector2D endCenter = ball.getPosition().added(new Vector2D(ball.getRadius(), ball.getRadius()));           // tâm bóng ở frame hiện tại

        Vector2D[][] expandedEdges = CollisionUtils.getSweptAabbEdges(ball, box);
        Vector2D[][] originalEdges = CollisionUtils.getSweptAabbEdges(null, box);
        CollisionResult bestResult = null;

        for (int edgeIndex = 0; edgeIndex < expandedEdges.length; edgeIndex++) {
            Vector2D intersection = CollisionUtils.getLineIntersection(
                    startCenter, endCenter,
                    expandedEdges[edgeIndex][0], expandedEdges[edgeIndex][1]
            );

            if (intersection != null) {
                if (CollisionUtils.isBetween(originalEdges[edgeIndex][0].x, originalEdges[edgeIndex][1].x, intersection.x) ||
                        CollisionUtils.isBetween(originalEdges[edgeIndex][0].y, originalEdges[edgeIndex][1].y, intersection.y)) {

                    Vector2D offset = intersection.subtracted(startCenter);
                    float time = offset.length() / ball.getVelocity().length();
                    Vector2D hitPoint = new Vector2D(
                            CollisionUtils.clamp(intersection.x, originalEdges[edgeIndex][0].x, originalEdges[edgeIndex][1].x),
                            CollisionUtils.clamp(intersection.y, originalEdges[edgeIndex][0].y, originalEdges[edgeIndex][1].y)
                    );
                    Vector2D normal = originalEdges[edgeIndex][1].subtracted(originalEdges[edgeIndex][0]).normalRight().normalized();
                    Vector2D reflectedVelocity = CollisionUtils.reflect(ball.getVelocity(), normal);
                    CollisionResult edgeResult = new CollisionResult(box, hitPoint, normal, time, reflectedVelocity);
                    if ((bestResult == null || time < bestResult.getTime() - Constants.COLLISION_EPSILON) && edgeResult.isValid(ball)) {
                        bestResult = edgeResult;
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
