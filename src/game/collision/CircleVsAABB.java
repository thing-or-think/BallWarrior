package game.collision;

import core.Constants;
import entity.Ball;
import entity.Brick;
import entity.Entity;
import entity.Paddle;
import utils.MathUtils;
import utils.Vector2D;

public class CircleVsAABB {

    /**
     * Detects collision between a ball and paddle
     */
//    public static CollisionResult intersect(Ball ball, Paddle paddle) {
//        return CollisionResult(ball, paddle);
//    }

    /**
     * Detects collision between a ball and brick
     */
//    public static CollisionResult intersect(Ball ball, Brick brick) {
//        return CollisionResult(ball, brick);
//    }

    /**
     * Detects continuous collision between a circle and AABB
     */
    public static CollisionResult CollisionResult(Ball ball, Entity aabb) {
        Vector2D previousPosition = ball.getPreviousPosition();
        Vector2D currentPosition = ball.getPosition();
        float radius = ball.getRadius();

        // Adjust positions to account for ball radius
        Vector2D adjustedPrev = new Vector2D(
                previousPosition.x + radius,
                previousPosition.y + radius
        );
        Vector2D adjustedCurr = new Vector2D(
                currentPosition.x + radius,
                currentPosition.y + radius
        );

        CollisionResult earliestCollision = null;
        float earliestCollisionTime = Float.MAX_VALUE;

        // Check collision with each edge of the AABB
        for (Edge edge : getEdges(aabb)) {
            CollisionResult collision = checkEdgeCollision(
                    ball, aabb, edge, adjustedPrev, adjustedCurr
            );

            if (collision != null && collision.getTime() < earliestCollisionTime) {
                earliestCollision = collision;
                earliestCollisionTime = collision.getTime();
            }
        }

        return earliestCollision;
    }

    /**
     * Checks collision with a specific edge of the AABB
     */
    private static CollisionResult checkEdgeCollision(Ball ball, Entity aabb, Edge edge,
                                                      Vector2D adjustedPrev, Vector2D adjustedCurr) {
        Vector2D intersection = MathUtils.getLineIntersection(
                adjustedPrev, adjustedCurr, edge.start, edge.end
        );

        if (intersection == null) {
            return checkCornerCollision(ball, aabb, edge, adjustedPrev, adjustedCurr);
        }

        return handleEdgeIntersection(ball, aabb, edge, adjustedPrev, adjustedCurr, intersection);
    }

    /**
     * Handles the case where the ball's path intersects with an edge
     */
    private static CollisionResult handleEdgeIntersection(Ball ball, Entity aabb, Edge edge,
                                                          Vector2D adjustedPrev, Vector2D adjustedCurr,
                                                          Vector2D intersection) {
        float radius = ball.getRadius();
        Vector2D edgeVector = edge.end.subtracted(edge.start);
        Vector2D edgeDirection = edgeVector.normalized();
        Vector2D edgeNormal = edgeDirection.normalLeft();

        Vector2D toPrev = adjustedPrev.subtracted(intersection);
        float angle = toPrev.angle(edgeDirection);

        // Calculate collision point considering ball radius
        float adjacent = (float) (radius * Math.tan(angle));
        float hypotenuse = (float) Math.sqrt(adjacent * adjacent + radius * radius);

        Vector2D contactPoint = intersection.added(
                edgeDirection.multiplied(adjacent / edgeVector.length())
        );
        Vector2D collisionPoint = intersection.added(
                adjustedPrev.subtracted(adjustedCurr).multiplied(hypotenuse / adjustedPrev.subtracted(adjustedCurr).length())
        );

        if (isPointOnSegment(contactPoint, edge.start, edge.end) &&
                isPointOnSegment(collisionPoint, adjustedPrev, adjustedCurr)) {

            float collisionTime = collisionPoint.subtracted(adjustedPrev).length() /
                    adjustedCurr.subtracted(adjustedPrev).length();

            Vector2D reflectedVelocity = reflect(ball.getVelocity(), edgeNormal);
            return new CollisionResult(aabb, contactPoint, edgeNormal, collisionTime, reflectedVelocity);
        }

        return checkCornerCollision(ball, aabb, edge, adjustedPrev, adjustedCurr);
    }

    /**
     * Checks for corner collisions when no edge intersection is found
     */
    private static CollisionResult checkCornerCollision(Ball ball, Entity aabb, Edge edge,
                                                        Vector2D adjustedPrev, Vector2D adjustedCurr) {
        float radius = ball.getRadius();

        // Check both corners of the edge
        CollisionResult cornerCollision = checkSingleCorner(
                ball, aabb, edge, adjustedPrev, adjustedCurr, edge.start
        );

        if (cornerCollision == null) {
            cornerCollision = checkSingleCorner(
                    ball, aabb, edge, adjustedPrev, adjustedCurr, edge.end
            );
        }

        return cornerCollision;
    }

    /**
     * Checks collision with a single corner point
     */
    private static CollisionResult checkSingleCorner(Ball ball, Entity aabb, Edge edge,
                                                     Vector2D adjustedPrev, Vector2D adjustedCurr,
                                                     Vector2D corner) {
        float radius = ball.getRadius();

        Vector2D closestPoint = MathUtils.projectPointToLine(corner, adjustedCurr, adjustedPrev);
        float distanceToPath = closestPoint.subtracted(corner).length();

        if (distanceToPath > radius) {
            return null;
        }

        float overlap = (float) Math.sqrt(radius * radius - distanceToPath * distanceToPath);
        Vector2D collisionPoint = closestPoint.added(
                adjustedPrev.subtracted(closestPoint).multiplied(overlap / adjustedPrev.subtracted(closestPoint).length())
        );

        if (!isPointOnSegment(collisionPoint, adjustedPrev, adjustedCurr)) {
            return null;
        }

        float collisionTime = collisionPoint.subtracted(adjustedPrev).length() /
                adjustedCurr.subtracted(adjustedPrev).length();

        Vector2D edgeNormal = edge.end.subtracted(edge.start).normalLeft();
        Vector2D reflectedVelocity = reflect(ball.getVelocity(), edgeNormal);

        return new CollisionResult(aabb, corner, edgeNormal, collisionTime, reflectedVelocity);
    }

    /**
     * Reflects a vector across a normal vector
     */
    private static Vector2D reflect(Vector2D vector, Vector2D normal) {
        float dotProduct = vector.dot(normal);
        return vector.subtracted(normal.multiplied(2 * dotProduct));
    }

    /**
     * Gets all edges of an AABB entity
     */
    private static Edge[] getEdges(Entity entity) {
        float x = entity.getX();
        float y = entity.getY();
        float width = entity.getWidth();
        float height = entity.getHeight();

        Vector2D topLeft = new Vector2D(x, y);
        Vector2D topRight = new Vector2D(x + width, y);
        Vector2D bottomLeft = new Vector2D(x, y + height);
        Vector2D bottomRight = new Vector2D(x + width, y + height);

        return new Edge[] {
                new Edge(topLeft, topRight),     // Top edge
                new Edge(topRight, bottomRight), // Right edge
                new Edge(bottomRight, bottomLeft), // Bottom edge
                new Edge(bottomLeft, topLeft)    // Left edge
        };
    }

    /**
     * Checks if a point lies on a line segment
     */
    private static boolean isPointOnSegment(Vector2D point, Vector2D segmentStart, Vector2D segmentEnd) {
        return point.x >= Math.min(segmentStart.x, segmentEnd.x) - Constants.COLLISION_EPSILON &&
                point.x <= Math.max(segmentStart.x, segmentEnd.x) + Constants.COLLISION_EPSILON &&
                point.y >= Math.min(segmentStart.y, segmentEnd.y) - Constants.COLLISION_EPSILON &&
                point.y <= Math.max(segmentStart.y, segmentEnd.y) + Constants.COLLISION_EPSILON;
    }


    /**
     * Helper class representing an edge with start and end points
     */
    private static class Edge {
        public final Vector2D start;
        public final Vector2D end;

        public Edge(Vector2D start, Vector2D end) {
            this.start = start;
            this.end = end;
        }
    }
}