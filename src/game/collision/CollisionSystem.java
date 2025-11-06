package game.collision;

import entity.Ball;
import utils.Constants;
import entity.*;
import utils.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Hệ thống kiểm soát va chạm.
 * Hỗ trợ ignoreList để xử lý Fireball.
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

    /**
     * Tìm va chạm gần nhất, NGOẠI TRỪ các entity trong danh sách ignored.
     * @param ball Quả bóng
     * @param ignoredEntities Danh sách các entity cần bỏ qua (có thể là null)
     */
    public CollisionResult findNearestCollision(Ball ball, List<Entity> ignoredEntities) {
        if (ball == null || colliders.isEmpty()) return null;
        // Gọi hàm helper với danh sách ignored
        return checkBallVsEntities(ball, colliders, ignoredEntities);
    }

    /** * Quá tải (Overload) phương thức cũ để tương thích
     * (Gọi mà không cần danh sách bỏ qua)
     */
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

        // Di chuyển bóng đến GẦN điểm va chạm (t - epsilon)
        float t = Math.max(0f, Math.min(1f, result.getTime() - 0.01f));
        Vector2D newCenter = prev.added(travel.multiplied(t));
        ball.setPosition(newCenter);

        // Đẩy bóng ra khỏi mặt va chạm 1 epsilon
        Vector2D pushOut = result.getNormal().normalized().multiplied(Constants.COLLISION_EPSILON);
        ball.setPosition(ball.getPosition().added(pushOut));

        // Cập nhật vận tốc sau phản xạ
        if (result.getEntity() instanceof Paddle) {
            Vector2D center = ball.getCenter();
            if (center.x < paddle.getX() + (int) (paddle.getWidth() / 2)) {
                ball.setVelocity(new Vector2D(-1, -1).normalized().multiplied(Constants.BALL_SPEED));
            } else {
                ball.setVelocity(new Vector2D(1, -1).normalized().multiplied(Constants.BALL_SPEED));
            }
        } else {
            ball.setVelocity(result.getReflectedVelocity());
        }

        // Xử lý hiệu ứng tùy loại entity
        Entity hit = result.getEntity();
        if (hit instanceof Brick) {
            ((Brick) hit).hit(1); // Gây 1 damage (nếu là bóng thường)
        } else if (hit instanceof Paddle) {
            CircleVsAABB.handleBallInsideEntity(ball, hit);
        }

        return true;
    }

    // ==================== Private Helpers ====================

    /** * Kiểm tra va chạm Ball với danh sách entity, bỏ qua danh sách ignored
     */
    private CollisionResult checkBallVsEntities(Ball ball, List<Entity> entities, List<Entity> ignoredEntities) {
        CollisionResult nearest = null;

        for (Entity e : entities) {
            if (e == null || e == ball) continue;

            // Bỏ qua nếu entity nằm trong danh sách ignored
            if (ignoredEntities != null && ignoredEntities.contains(e)) {
                continue;
            }

            // Broad-phase check
            if (!broadPhaseCheck(ball, e)) continue;

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
