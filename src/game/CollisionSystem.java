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

    public static void handleBallCollision(Ball ball, Entity entity, boolean isPaddle) {
        Vector2D prev = ball.getPreviousPosition();
        Vector2D curr = ball.getPosition();

        if (prev.equals(curr)) {
            return;
        }

        int w = ball.getWidth();
        int h = ball.getHeight();

        // Lấy cạnh entity
        Vector2D[][] edges = getEdges(entity);
        Vector2D[][] ballEdges = {
                {new Vector2D(prev.x + w / 2.0f, prev.y + h), new Vector2D(curr.x + w / 2.0f, curr.y + h)},
                {new Vector2D(prev.x, prev.y + h / 2.0f), new Vector2D(curr.x, curr.y + h / 2.0f)},
                {new Vector2D(prev.x + w / 2.0f, prev.y), new Vector2D(curr.x + w / 2.0f, curr.y)},
                {new Vector2D(prev.x + w, prev.y + h / 2.0f), new Vector2D(curr.x + w, curr.y + h / 2.0f)}
        };

        Vector2D nearestIntersection = null;
        double nearestDist = Double.MAX_VALUE;
        int hitEdge = -1;

        for (int i = 0; i < edges.length; i++) {
            nearestIntersection = MathUtils.getLineIntersection(ballEdges[i][0], ballEdges[i][1], edges[i][0], edges[i][1]);
            if (nearestIntersection != null) {
                hitEdge = i;
                break;
            }
        }

        if (nearestIntersection != null) {
            // Đặt lại vị trí đúng điểm va chạm

            if (hitEdge == 0) { // top
                nearestIntersection.set(nearestIntersection.x - w / 2.0f, nearestIntersection.y - h);
                if (!nearestIntersection.equals(ball.getPreviousPosition())) {
                    ball.setPosition(nearestIntersection.x, nearestIntersection.y);
                }
                ball.setVelocity(ball.getVelocity().x, -Math.abs(ball.getVelocity().y));
            } else if (hitEdge == 2) { // bottom
                nearestIntersection.set(nearestIntersection.x - w / 2.0f, nearestIntersection.y);
                if (!nearestIntersection.equals(ball.getPreviousPosition())) {
                    ball.setPosition(nearestIntersection.x, nearestIntersection.y);
                }
                ball.setVelocity(ball.getVelocity().x, Math.abs(ball.getVelocity().y));
            } else if (hitEdge == 1) { // right
                nearestIntersection.set(nearestIntersection.x, nearestIntersection.y - h / 2.0f);
                if (!nearestIntersection.equals(ball.getPreviousPosition())) {
                    ball.setPosition(Math.min(Constants.WIDTH - ball.getWidth(),  nearestIntersection.x), nearestIntersection.y);
                }
                ball.setVelocity(Math.abs(ball.getVelocity().x), ball.getVelocity().y);
            } else if (hitEdge == 3) { // left
                nearestIntersection.set(nearestIntersection.x - w, nearestIntersection.y - h / 2.0f);
                if (!nearestIntersection.equals(ball.getPreviousPosition())) {
                    ball.setPosition(Math.max(0.0f, nearestIntersection.x), nearestIntersection.y);
                }
                ball.setVelocity(-Math.abs(ball.getVelocity().x), ball.getVelocity().y);
            }

            if (isPaddle) {
                // logic riêng cho paddle
            }
        }
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
