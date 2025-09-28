package utils;

import core.Constants;

public class MathUtils {

    private static boolean isBetween(float a, float b, float target) {
        return target >= Math.min(a, b) - Constants.COLLISION_EPSILON &&
                target <= Math.max(a, b) + Constants.COLLISION_EPSILON;
    }

    /**
     * Tính giao điểm giữa 2 đoạn thẳng AB và CD.
     * @return Vector2D giao điểm hoặc null nếu không cắt.
     */
    public static Vector2D getLineIntersection(Vector2D A, Vector2D B, Vector2D C, Vector2D D) {
        float a1 = B.y - A.y;
        float b1 = A.x - B.x;
        float c1 = a1 * A.x + b1 * A.y;

        float a2 = D.y - C.y;
        float b2 = C.x - D.x;
        float c2 = a2 * C.x + b2 * C.y;

        float det = a1 * b2 - a2 * b1;
        if (Math.abs(det) < Constants.COLLISION_EPSILON) return null; // song song hoặc trùng

        float x = (b2 * c1 - b1 * c2) / det;
        float y = (a1 * c2 - a2 * c1) / det;

        // Kiểm tra (x,y) có nằm trên cả 2 đoạn thẳng không
        if (isBetween(A.x, B.x, x) && isBetween(A.y, B.y, y) &&
                isBetween(C.x, D.x, x) && isBetween(C.y, D.y, y)) {
            return new Vector2D(x, y);
        }
        return null;
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
