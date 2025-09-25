package utils;

import core.Constants;

public class MathUtils {

    private static boolean isBetween(float a, float b, float target) {
        return target >= Math.min(a, b) - Constants.COLLISION_EPSILON &&
                target <= Math.max(a, b) + Constants.COLLISION_EPSILON;
    }

    /**
     * Tìm giao điểm của hai đoạn thẳng AB và CD.
     * Nếu có giao điểm trả về Vector2D, ngược lại trả về null.
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
}
