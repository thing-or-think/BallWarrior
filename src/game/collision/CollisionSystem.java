package game.collision;

import utils.Constants;
import entity.*;
import utils.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Hệ thống kiểm soát va chạm đơn giản cho các Ball vs Entity (Paddle, Brick,...)
 * - Sửa lỗi broad-phase (swept AABB) và sử dụng interpolation giữa previous->current khi đặt vị trí sau va chạm.
 * - Đẩy bóng ra khỏi mặt va chạm một khoảng epsilon để tránh "sticking".
 * - Trả về CollisionResult gần nhất trong frame.
 */
public class CollisionSystem {

    // ==================== Fields ====================
    private Paddle paddle;
    private final List<Entity> colliders;

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

    /** Tìm va chạm gần nhất giữa Ball và toàn bộ colliders */
    public CollisionResult findNearestCollision(Ball ball) {
        if (ball == null || colliders.isEmpty()) return null;
        return checkBallVsEntities(ball, colliders);
    }

    /** Xử lý kết quả va chạm, trả về true nếu bóng thay đổi trạng thái */
    public boolean resolveCollision(Ball ball, CollisionResult result) {
        if (CircleVsAABB.handleBallInsideEntity(ball, paddle) || result == null || ball == null)
            return false;

        Vector2D prev = ball.getPreviousPosition();
        Vector2D curr = ball.getPosition();
        Vector2D travel = curr.subtracted(prev);

        // Di chuyển bóng đến gần điểm va chạm
        float t = Math.max(0f, Math.min(1f, result.getTime() - 0.01f));
        Vector2D newCenter = prev.added(travel.multiplied(t));
        ball.setPosition(newCenter);

        // Đẩy bóng ra khỏi mặt va chạm 1 epsilon
        Vector2D pushOut = result.getNormal().normalized().multiplied(Constants.COLLISION_EPSILON);
        ball.setPosition(ball.getPosition().added(pushOut));

        // Cập nhật vận tốc sau phản xạ
        ball.setVelocity(result.getReflectedVelocity());

        // Xử lý hiệu ứng tùy loại entity
        Entity hit = result.getEntity();
        if (hit instanceof Brick) {
            ((Brick) hit).hit(1);
        } else if (hit instanceof Paddle) {
            CircleVsAABB.handleBallInsideEntity(ball, hit);
            // Có thể thêm logic điều chỉnh góc theo vị trí chạm
        }

        return true;
    }

    // ==================== Private Helpers ====================
    /** Kiểm tra va chạm Ball với danh sách entity (Paddle, Brick…) */
    private CollisionResult checkBallVsEntities(Ball ball, List<Entity> entities) {
        CollisionResult nearest = null;

        for (Entity e : entities) {
            if (e == null || e == ball) continue;

            // Broad-phase check
            if (!broadPhaseCheck(ball, e)) continue;

            // Logic đặc biệt cho Fireball vs Brick
//            if (ball.isFireBall() && e instanceof Brick) {
//                CollisionResult result = CircleVsAABB.intersect(ball, e);
//                if (result != null) {
//                    // Fireball va chạm với gạch, trả về một kết quả va chạm đặc biệt.
//                    // Điều này cho phép GameScene phá hủy gạch nhưng không xử lý phản xạ.
//                    return new CollisionResult(e, new Vector2D(), new Vector2D(), 0);
//                }
//            }

            // Narrow-phase check
            CollisionResult result = null;
            if (e instanceof Paddle || e instanceof Brick || e instanceof Shield) {
                result = CircleVsAABB.intersect(ball, e);
            }

            // Chọn va chạm gần nhất
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
