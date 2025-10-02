package game;

import core.Constants;
import entity.Ball;
import entity.Entity;
import utils.MathUtils;
import utils.Vector2D;

public class CollisionSystem {

    // Kiểm tra va chạm AABB (Axis-Aligned Bounding Box)
    public static boolean checkCollision(Entity a, Entity b) {
        return a.getX() < b.getX() + b.getWidth() + Constants.COLLISION_EPSILON &&
                a.getX() + a.getWidth() > b.getX() - Constants.COLLISION_EPSILON &&
                a.getY() < b.getY() + b.getHeight() + Constants.COLLISION_EPSILON &&
                a.getY() + a.getHeight() > b.getY() - Constants.COLLISION_EPSILON;
    }

    private static Vector2D[][] getEdges(Entity entity) {
        Vector2D topLeft = new Vector2D(entity.getX(), entity.getY());
        Vector2D topRight = new Vector2D(entity.getX() + entity.getWidth(), entity.getY());
        Vector2D bottomLeft = new Vector2D(entity.getX(), entity.getY() + entity.getHeight());
        Vector2D bottomRight = new Vector2D(entity.getX() + entity.getWidth(), entity.getY() + entity.getHeight());

        return new Vector2D[][] {
                {topLeft, topRight},
                {topRight, bottomRight},
                {bottomRight, bottomLeft},
                {bottomLeft, topLeft}
        };
    }

    public static boolean handleBallCollision(Ball ball, Entity entity, boolean isPaddleOrShield) {
        Vector2D prev = ball.getPreviousPosition();
        Vector2D curr = ball.getPosition();

        if (prev.equals(curr)) return false;

        int w = ball.getWidth();
        int h = ball.getHeight();

        // Các cạnh entity
        Vector2D[][] edges = getEdges(entity);

        // Các đoạn đường bóng di chuyển (sweep test 4 cạnh)
        Vector2D[][] ballEdges = {
                {new Vector2D(prev.x + w / 2.0f, prev.y + h), new Vector2D(curr.x + w / 2.0f, curr.y + h)},   // bottom
                {new Vector2D(prev.x, prev.y + h / 2.0f), new Vector2D(curr.x, curr.y + h / 2.0f)},           // left
                {new Vector2D(prev.x + w / 2.0f, prev.y), new Vector2D(curr.x + w / 2.0f, curr.y)},           // top
                {new Vector2D(prev.x + w, prev.y + h / 2.0f), new Vector2D(curr.x + w, curr.y + h / 2.0f)}    // right
        };

        Vector2D nearestIntersection = null;
        double nearestDist = Double.MAX_VALUE;
        int hitEdge = -1;

        // Duyệt tất cả cạnh entity và cạnh bóng
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < ballEdges.length; j++) {
                Vector2D inter = MathUtils.getLineIntersection(
                        ballEdges[j][0], ballEdges[j][1],
                        edges[i][0], edges[i][1]
                );
                if (inter != null) {
                    double dist = inter.distance(prev);
                    if (dist < nearestDist) {
                        nearestDist = dist;
                        nearestIntersection = inter;
                        hitEdge = i;
                    }
                }
            }
        }

        if (nearestIntersection != null) {
            float epsilon = Constants.COLLISION_EPSILON;

            // Chỉnh lại vị trí + velocity dựa vào cạnh trúng
            if (hitEdge == 0) { // top edge
                ball.setPosition(curr.x, entity.getY() - h - epsilon);
                ball.setVelocity(ball.getVelocity().x, -Math.abs(ball.getVelocity().y));
            } else if (hitEdge == 2) { // bottom edge
                ball.setPosition(curr.x, entity.getY() + entity.getHeight() + epsilon);
                ball.setVelocity(ball.getVelocity().x, Math.abs(ball.getVelocity().y));
            } else if (hitEdge == 1) { // right edge
                ball.setPosition(entity.getX() + entity.getWidth() + epsilon, curr.y);
                ball.setVelocity(Math.abs(ball.getVelocity().x), ball.getVelocity().y);
            } else if (hitEdge == 3) { // left edge
                ball.setPosition(entity.getX() - w - epsilon, curr.y);
                ball.setVelocity(-Math.abs(ball.getVelocity().x), ball.getVelocity().y);
            }

            if (isPaddleOrShield) {
                // Nếu là paddle → đổi góc theo vị trí chạm
                if (entity.getHeight() == Constants.PADDLE_HEIGHT) {
                    float paddleCenter = entity.getX() + entity.getWidth() / 2.0f;
                    float hitPos = (ball.getX() + w / 2.0f) - paddleCenter;
                    float ratio = hitPos / (entity.getWidth() / 2.0f);
                    float newVelX = ratio * Constants.BALL_SPEED;
                    float newVelY = -Math.abs(ball.getVelocity().y);
                    ball.setVelocity(newVelX, newVelY);
                }
                // Nếu là shield → chỉ nảy thẳng lên
                else {
                    ball.setVelocity(ball.getVelocity().x, -Math.abs(ball.getVelocity().y));
                }
            }

            // Đảm bảo velocity không quá nhỏ → tránh bị "dính"
            float vx = ball.getVelocity().x;
            float vy = ball.getVelocity().y;

            if (Math.abs(vx) < 0.5f) {
                vx = (vx == 0 ? 0.5f : Math.signum(vx) * 0.5f);
            }
            if (Math.abs(vy) < 0.5f) {
                vy = (vy == 0 ? 0.5f : Math.signum(vy) * 0.5f);
            }

            ball.setVelocity(vx, vy);

            return true;
        }
        return false;
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
}
