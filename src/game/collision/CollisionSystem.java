package game.collision;

import entity.Ball;
import utils.Constants;
import entity.*;
import utils.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Hệ thống kiểm soát va chạm.
 */
public class CollisionSystem {

    // ==================== Fields ====================
    private Paddle paddle;
    private final List<Entity> colliders;
    private static final float MAX_ANGLE_RADIANS = (float) Math.toRadians(75);

    // ==================== Constructor ====================
    public CollisionSystem(Paddle paddle) {
        this.colliders = new ArrayList<>();
        this.paddle = paddle;
    }

    // ==================== Public API ====================
    /** Đăng ký entity có thể va chạm */
    public void register(Entity e) {
        if (e == null) return;
        if (!colliders.contains(e)) {
            colliders.add(e);
        }
    }

    /** Hủy đăng ký entity */
    public void unregister(Entity e) {
        colliders.remove(e);
        colliders.removeIf(entity -> !entity.isAlive());
    }

    /**
     * Tìm va chạm gần nhất, NGOẠI TRỪ các entity trong danh sách ignored.
     */
    public CollisionResult findNearestCollision(Ball ball, List<Entity> ignoredEntities) {
        if (ball == null || colliders.isEmpty()) return null;
        return checkBallVsEntities(ball, colliders, ignoredEntities);
    }

    /** Quá tải (Overload) phương thức cũ để tương thích */
    public CollisionResult findNearestCollision(Ball ball) {
        return findNearestCollision(ball, null);
    }

    /** Xử lý kết quả va chạm, trả về true nếu bóng thay đổi trạng thái */
    public boolean resolveCollision(Ball ball, CollisionResult result) {
        if (result == null || ball == null)
            return false;

        Vector2D prev = ball.getPreviousPosition();
        Vector2D curr = ball.getPosition(); // Vị trí cuối frame (nếu không va chạm)
        Vector2D travel = curr.subtracted(prev); // Vector di chuyển của cả frame

        float t = Math.max(0f, Math.min(1f, result.getTime() - 0.01f));
        Vector2D newCenter = prev.added(travel.multiplied(t));
        ball.setPosition(newCenter);

        Vector2D pushOut = result.getNormal().normalized().multiplied(Constants.COLLISION_EPSILON);
        ball.setPosition(ball.getPosition().added(pushOut));

        if (result.getEntity() instanceof Paddle) {

            Vector2D ballCenter = ball.getCenter();
            float paddleCenter = paddle.getX() + (paddle.getWidth() / 2f);
            float offset = ballCenter.x - paddleCenter;

            float normalizedOffset = offset / (paddle.getWidth() / 2f);

            normalizedOffset = Math.max(-1f, Math.min(1f, normalizedOffset));

            float newAngle = (float) (Math.toRadians(90) - (normalizedOffset * MAX_ANGLE_RADIANS));

            float newVx = (float) Math.cos(newAngle) * Constants.BALL_SPEED;
            float newVy = (float) -Math.sin(newAngle) * Constants.BALL_SPEED;

            ball.setVelocity(new Vector2D(newVx, newVy));

        } else {
            ball.setVelocity(result.getReflectedVelocity());
        }

        Entity hit = result.getEntity();
        if (hit instanceof Brick) {
            ((Brick) hit).hit(1); // Gây 1 damage (nếu là bóng thường)
        } else if (hit instanceof Paddle) {
            CircleVsAABB.handleBallInsideEntity(ball, hit);
        }

        return true;
    }

    // ==================== Private Helpers ====================

    private CollisionResult checkBallVsEntities(Ball ball, List<Entity> entities, List<Entity> ignoredEntities) {
        CollisionResult nearest = null;

        for (Entity e : entities) {
            if (e == null || e == ball) continue;

            if (ignoredEntities != null && ignoredEntities.contains(e)) {
                continue;
            }

            if (!broadPhaseCheck(ball, e)) continue;

            CollisionResult result = null;
            if (e instanceof Paddle || e instanceof Brick || e instanceof Shield) {
                result = CircleVsAABB.intersect(ball, e);
            }

            if (result != null && (nearest == null || result.getTime() < nearest.getTime())) {
                nearest = result;
            }
        }

        return nearest;
    }

    /** Broad-phase check bằng AABB bao phủ đường đi của ball (swept AABB) */
    private boolean broadPhaseCheck(Ball ball, Entity entity) {
        Vector2D prev = ball.getPreviousPosition();
        Vector2D curr = ball.getPosition();

        float w = ball.getWidth();
        float h = ball.getHeight();

        float ballMinX = Math.min(prev.x, curr.x);
        float ballMinY = Math.min(prev.y, curr.y);
        float ballMaxX = Math.max(prev.x + w, curr.x + w);
        float ballMaxY = Math.max(prev.y + h, curr.y + h);

        float entityMinX = entity.getPosition().x;
        float entityMinY = entity.getPosition().y;
        float entityMaxX = entityMinX + entity.getWidth();
        float entityMaxY = entityMinY + entity.getHeight();

        return (ballMinX <= entityMaxX && ballMaxX >= entityMinX &&
                ballMinY <= entityMaxY && ballMaxY >= entityMinY);
    }
}
