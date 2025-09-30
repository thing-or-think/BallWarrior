package game.collision;

import core.Constants;
import utils.Vector2D;

public class CollisionUtils {

    protected static boolean isBetween(float a, float b, float target) {
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

    public static Vector2D[] circleLineIntersection(Vector2D C, float r, Vector2D P1, Vector2D P2) {
        // Vector từ P1 -> P2
        Vector2D d = P2.subtracted(P1);
        Vector2D f = P1.subtracted(C);

        float a = d.dot(d);
        float b = 2 * f.dot(d);
        float c = f.dot(f) - r * r;

        float discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            // Không có giao điểm
            return new Vector2D[0];
        } else if (Math.abs(discriminant) < 1e-6) {
            // 1 điểm giao (tiếp xúc)
            float t = -b / (2 * a);
            Vector2D P = P1.added(d.multiplied(t));
            return new Vector2D[]{P};
        } else {
            // 2 điểm giao
            float sqrtD = (float) Math.sqrt(discriminant);
            float t1 = (-b - sqrtD) / (2 * a);
            float t2 = (-b + sqrtD) / (2 * a);

            Vector2D P1g = P1.added(d.multiplied(t1));
            Vector2D P2g = P1.added(d.multiplied(t2));

            return new Vector2D[]{P1g, P2g};
        }
    }

    public static Vector2D[] circleSegmentIntersection(Vector2D C, float r, Vector2D P1, Vector2D P2) {
        Vector2D[] points = circleLineIntersection(C, r, P1, P2); // gọi hàm line vô hạn
        Vector2D d = P2.subtracted(P1);
        int count = 0;
        Vector2D[] result = new Vector2D[2]; // tối đa 2 điểm

        for (Vector2D p : points) {
            if (p == null) continue;
            // Tính t của điểm giao: P = P1 + t * d => t = (p - P1) / d
            float tX = d.x != 0 ? (p.x - P1.x) / d.x : Float.NaN;
            float tY = d.y != 0 ? (p.y - P1.y) / d.y : Float.NaN;

            float t = Float.isNaN(tX) ? tY : Float.isNaN(tY) ? tX : (tX + tY) * 0.5f;

            if (isBetween(0f, 1f, t)) {
                result[count++] = p;
            }
        }

        if (count == 0) return new Vector2D[0];
        if (count == 1) return new Vector2D[]{result[0]};
        return new Vector2D[]{result[0], result[1]};
    }

}
