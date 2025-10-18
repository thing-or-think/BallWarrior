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
        // Nếu bóng không di chuyển, không có va chạm liên tục nào xảy ra
        if (ball.getVelocity().x == 0 && ball.getVelocity().y == 0) {
            return null;
        }

        // Coi bóng như một điểm và mở rộng viên gạch (box) ra bằng bán kính của bóng.
        // Đây là một phần của kỹ thuật Minkowski Sum.
        float radius = ball.getRadius();
        Vector2D expandedMin = new Vector2D(box.getX() - radius, box.getY() - radius);
        Vector2D expandedMax = new Vector2D(box.getX() + box.getWidth() + radius, box.getY() + box.getHeight() + radius);

        // Vị trí bắt đầu của tâm bóng trong frame này
        Vector2D startPoint = ball.getPreviousPosition().added(new Vector2D(radius, radius));

        // Vận tốc của bóng
        Vector2D velocity = ball.getVelocity();

        // Tính thời gian để tia đi từ startPoint cắt các mặt phẳng của expandedBox.
        // Xử lý cẩn thận trường hợp vận tốc bằng 0 để tránh chia cho 0.
        float t_near_x, t_far_x, t_near_y, t_far_y;

        if (velocity.x == 0) {
            // Nếu bóng không di chuyển theo trục X
            if (startPoint.x < expandedMin.x || startPoint.x > expandedMax.x) {
                return null; // Bóng nằm ngoài và không bao giờ cắt được "slab" dọc
            }
            t_near_x = -Float.MAX_VALUE;
            t_far_x = Float.MAX_VALUE;
        } else {
            t_near_x = (expandedMin.x - startPoint.x) / velocity.x;
            t_far_x = (expandedMax.x - startPoint.x) / velocity.x;
        }

        if (velocity.y == 0) {
            // Nếu bóng không di chuyển theo trục Y
            if (startPoint.y < expandedMin.y || startPoint.y > expandedMax.y) {
                return null; // Bóng nằm ngoài và không bao giờ cắt được "slab" ngang
            }
            t_near_y = -Float.MAX_VALUE;
            t_far_y = Float.MAX_VALUE;
        } else {
            t_near_y = (expandedMin.y - startPoint.y) / velocity.y;
            t_far_y = (expandedMax.y - startPoint.y) / velocity.y;
        }

        // Đảm bảo t_near luôn nhỏ hơn t_far trên mỗi trục
        if (t_near_x > t_far_x) { float temp = t_near_x; t_near_x = t_far_x; t_far_x = temp; }
        if (t_near_y > t_far_y) { float temp = t_near_y; t_near_y = t_far_y; t_far_y = temp; }

        // Thời gian va chạm thực sự là thời điểm "vào" cuối cùng và thời điểm "ra" sớm nhất.
        float t_entry = Math.max(t_near_x, t_near_y);
        float t_exit = Math.min(t_far_x, t_far_y);

        // Nếu không có khoảng thời gian giao nhau, hoặc va chạm không xảy ra trong frame này -> không va chạm.
        if (t_entry >= t_exit || t_entry > 1.0f || t_entry < 0.0f) {
            return null;
        }

        // Đã xác nhận có va chạm. Bây giờ xác định pháp tuyến (normal) của bề mặt va chạm.
        Vector2D normal;
        if (t_near_x > t_near_y) {
            // Va chạm xảy ra trên mặt phẳng dọc (trái hoặc phải)
            normal = new Vector2D(-Math.signum(velocity.x), 0);
        } else {
            // Va chạm xảy ra trên mặt phẳng ngang (trên hoặc dưới)
            normal = new Vector2D(0, -Math.signum(velocity.y));
        }

        // Trả về kết quả va chạm với thời gian (t_entry) và pháp tuyến chính xác.
        Vector2D hitPoint = startPoint.added(velocity.multiplied(t_entry));
        Vector2D reflectedVelocity = CollisionUtils.reflect(ball.getVelocity(), normal);

        return new CollisionResult(box, hitPoint, normal, t_entry, reflectedVelocity);
    }

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
