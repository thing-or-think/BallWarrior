package game.collision;

import entity.*;
import utils.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class CollisionSystem {

    private final List<Entity> colliders;

    public CollisionSystem() {
        this.colliders = new ArrayList<>();
    }

    // Đăng ký entity có thể va chạm
    public void register(Entity e) {
        if (!colliders.contains(e)) {
            colliders.add(e);
        }
    }

    // Hủy đăng ký entity
    public void unregister(Entity e) {
        colliders.remove(e);
    }

    /**
     * Kiểm tra va chạm Ball với danh sách entity (Paddle, Brick…)
     */
    private CollisionResult checkBallVsEntities(Ball ball, List<Entity> entities) {
        CollisionResult nearest = null;

        for (Entity e : entities) {
            if (e == ball) continue;

            // Broad-phase check (lọc sơ bộ bằng AABB)
            if (!broadPhaseCheck(ball, e)) continue;

            // Thuật toán va chạm cụ thể
            CollisionResult result = null;
            if (e instanceof Paddle || e instanceof Brick) {
                result = CircleVsAABB.CollisionResult(ball, e);
            }
            // Nếu có thêm loại entity khác thì mở rộng ở đây

            // Chọn collision có t nhỏ nhất (gần nhất)
            if (result != null) {
                if (nearest == null || result.getTime() < nearest.getTime()) {
                    nearest = result;
                }
            }
        }

        return nearest;
    }

    /**
     * Tìm va chạm gần nhất giữa Ball và toàn bộ colliders
     */
    public CollisionResult findNearestCollision(Ball ball) {
        return checkBallVsEntities(ball, colliders);
    }

    /**
     * Xử lý kết quả va chạm
     */
    public boolean resolveCollision(Ball ball, CollisionResult result) {
        if (result == null) return false;

        // Cập nhật vận tốc bóng sau khi phản xạ
        ball.setVelocity(result.getReflectedVelocity());

        // Tùy loại entity mà xử lý
        if (result.getEntity() instanceof Brick brick) {
            brick.hit();
        }
        return true;
    }

    /**
     * Broad-phase check bằng AABB bao phủ đường đi của ball
     */
    private boolean broadPhaseCheck(Ball ball, Entity entity) {
        Vector2D prev = ball.getPreviousPosition();
        Vector2D curr = ball.getPosition();

        float minX = Math.min(prev.x, curr.x);
        float minY = Math.min(prev.y, curr.y);
        float maxX = Math.max(prev.x + ball.getWidth(), curr.x + ball.getWidth());
        float maxY = Math.max(prev.y + ball.getHeight(), curr.y + ball.getHeight());

        // AABB ball trong frame
        float ballMinX = minX;
        float ballMinY = minY;
        float ballMaxX = maxX;
        float ballMaxY = maxY;

        // AABB entity
        float entityMinX = entity.getPosition().x;
        float entityMinY = entity.getPosition().y;
        float entityMaxX = entityMinX + entity.getWidth();
        float entityMaxY = entityMinY + entity.getHeight();

        // Kiểm tra overlap AABB
        return (ballMinX <= entityMaxX && ballMaxX >= entityMinX &&
                ballMinY <= entityMaxY && ballMaxY >= entityMinY);
    }
}
