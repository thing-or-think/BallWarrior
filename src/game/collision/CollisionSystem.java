package game.collision;

import core.Constants;
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

    private final List<Entity> colliders;

    public CollisionSystem() {
        this.colliders = new ArrayList<>();
    }

    // Đăng ký entity có thể va chạm
    public void register(Entity e) {
        if (e == null) return;
        if (!colliders.contains(e)) {
            colliders.add(e);
        }
    }

    // Hủy đăng ký entity
    public void unregister(Entity e) {
        colliders.remove(e);
    }

    /**
     * Tìm va chạm gần nhất giữa Ball và toàn bộ colliders
     */
    public CollisionResult findNearestCollision(Ball ball) {
        if (ball == null || colliders.isEmpty()) return null;
        return checkBallVsEntities(ball, colliders);
    }

    /**
     * Kiểm tra va chạm Ball với danh sách entity (Paddle, Brick…)
     */
    private CollisionResult checkBallVsEntities(Ball ball, List<Entity> entities) {
        CollisionResult nearest = null;

        for (Entity e : entities) {
            if (e == null || e == ball) continue;

            // Broad-phase check (lọc sơ bộ bằng AABB của đường đi của bóng)
            if (!broadPhaseCheck(ball, e)) continue;

            // Thuật toán va chạm cụ thể
            CollisionResult result = null;
            if (e instanceof Paddle || e instanceof Brick) {
                result = CircleVsAABB.intersect(ball, e);
            }
            // Nếu có thêm loại entity khác thì mở rộng ở đây

            // Chọn collision có t nhỏ nhất (gần nhất)
            if (result != null && (nearest == null || result.getTime() < nearest.getTime())) {
                nearest = result;
            }
        }

        return nearest;
    }

    /**
     * Xử lý kết quả va chạm
     * Trả về true nếu đã xử lý va chạm (ball thay đổi trạng thái)
     */
    public boolean resolveCollision(Ball ball, CollisionResult result) {
        if (result == null || ball == null || !result.isValid()) return false;

        Vector2D prev = ball.getPreviousPosition();
        Vector2D curr = ball.getPosition();
        Vector2D travel = curr.subtracted(prev);

        // Di chuyển bóng đến gần điểm va chạm (interpolate giữa prev->curr theo t)
        float t = result.getTime() - 0.01f;
        t = Math.max(0f, Math.min(1f, t));
        Vector2D newCenter = prev.added(travel.multiplied(t));
        ball.setPosition(newCenter);

        // Đẩy bóng ra khỏi mặt va chạm 1 epsilon nhỏ để tránh dính
        Vector2D pushOut = result.getNormal().normalized().multiplied(Constants.COLLISION_EPSILON);
        ball.setPosition(ball.getPosition().added(pushOut));

        // Cập nhật vận tốc bóng sau khi phản xạ
        ball.setVelocity(result.getReflectedVelocity());

        // Tùy loại entity mà xử lý thêm
        Entity hit = result.getEntity();
        if (hit instanceof Brick) {
            ((Brick) hit).hit();
        } else if (hit instanceof Paddle) {
            CircleVsAABB.handleBallInsideEntity(ball, hit);
            // Nếu muốn hiệu ứng theo vị trí chạm (ví dụ điều chỉnh góc),
            // bạn có thể thêm logic ở đây (dựa trên result.getHitPoint() hoặc vị trí tương đối trên paddle).
        }

        return true;
    }

    /**
     * Broad-phase check bằng AABB bao phủ đường đi của ball (swept AABB).
     * Sửa lỗi so với phiên bản cũ: tính đúng min/max của đoạn prev->curr cùng kích thước bóng.
     */
    private boolean broadPhaseCheck(Ball ball, Entity entity) {
        Vector2D prev = ball.getPreviousPosition();
        Vector2D curr = ball.getPosition();

        float w = ball.getWidth();
        float h = ball.getHeight();

        float ballMinX = Math.min(prev.x, curr.x);
        float ballMinY = Math.min(prev.y, curr.y);
        float ballMaxX = Math.max(prev.x + w, curr.x + w);
        float ballMaxY = Math.max(prev.y + h, curr.y + h);

        // AABB entity
        float entityMinX = entity.getPosition().x;
        float entityMinY = entity.getPosition().y;
        float entityMaxX = entityMinX + entity.getWidth();
        float entityMaxY = entityMinY + entity.getHeight();

        // Kiểm tra overlap AABB (nếu không overlap thì chắc chắn không va chạm trong frame)
        return (ballMinX <= entityMaxX && ballMaxX >= entityMinX &&
                ballMinY <= entityMaxY && ballMaxY >= entityMinY);
    }

}
