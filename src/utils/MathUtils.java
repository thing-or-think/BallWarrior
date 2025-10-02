package utils;

import core.Constants;

public class MathUtils {

    private static boolean isBetween(float a, float b, float target) {
        return target >= Math.min(a, b) - Constants.COLLISION_EPSILON &&
                target <= Math.max(a, b) + Constants.COLLISION_EPSILON;
    }

    public static float distancePointToLine(Vector2D point, Vector2D lineStart, Vector2D lineEnd) {
        Vector2D AB = lineEnd.subtracted(lineStart);
        Vector2D AP = point.subtracted(lineStart);

        float abLength = AB.length();
        if (abLength < Constants.COLLISION_EPSILON) {
            return AP.length();
        }

        return Math.abs(AB.cross(AP)) / abLength;
    }

    public static Vector2D[] getPointsAtDistance(Vector2D A, Vector2D B, float d) {
        // Vector AB
        Vector2D AB = B.subtracted(A);

        // Vector pháp tuyến 1 và 2
        Vector2D n1 = new Vector2D(-AB.y, AB.x);
        Vector2D n2 = new Vector2D(AB.y, -AB.x);

        // Chuẩn hóa
        n1 = n1.normalized();
        n2 = n2.normalized();

        // Tìm 2 điểm cách đường thẳng AB khoảng cách d
        Vector2D P1 = A.added(n1.multiplied(d));
        Vector2D P2 = A.added(n2.multiplied(d));

        return new Vector2D[]{P1, P2};
    }

    public static Vector2D projectPointToLine(Vector2D point, Vector2D lineStart, Vector2D lineEnd) {
        Vector2D AB = lineEnd.subtracted(lineStart);
        Vector2D AP = point.subtracted(lineStart);

        float abLenSq = AB.dot(AB);
        if (abLenSq < Constants.COLLISION_EPSILON) {
            // lineStart và lineEnd trùng nhau → hình chiếu chính là lineStart
            return new Vector2D(lineStart.x, lineStart.y);
        }

        float t = AP.dot(AB) / abLenSq;
        Vector2D H = lineStart.added(AB.multiplied(t));

        return H;
    }

}