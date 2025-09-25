package utils;

import core.Constants;

public class Vector2D {
    // 1. Fields
    public float x;
    public float y;

    // 2. Constructor
    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // 3. Getter / Setter

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // 4. ToString (hữu ích khi debug/log)

    // 5. Public methods chính
    public void add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public Vector2D added(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public void subtract(Vector2D other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public Vector2D subtracted(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Vector2D other) {
        return Math.abs(this.x - other.x) < Constants.COLLISION_EPSILON && Math.abs(this.y - other.y) < Constants.COLLISION_EPSILON;
    }

    public float dot(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    public float cross(Vector2D other) {
        return this.x * other.y - this.y * other.x;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float distance(Vector2D other) {
        return this.subtracted(other).length();
    }

    public float angle(Vector2D other) {
        float dot = this.dot(other);
        float len1 = this.length();
        float len2 = other.length();

        if (len1 < Constants.COLLISION_EPSILON || len2 < Constants.COLLISION_EPSILON) {
            return 0f; // vector quá nhỏ coi như không có góc
        }

        float cos = dot / (len1 * len2);
        cos = Math.max(-1f, Math.min(1f, cos)); // kẹp vào [-1, 1]

        float angle = (float) Math.acos(cos); // độ lớn góc (0..π)

        // cross product 2D để xác định chiều
        float cross = this.x * other.y - this.y * other.x;
        if (cross < 0) {
            angle = -angle; // đổi thành góc âm nếu quay theo chiều kim đồng hồ
        }

        return angle; // radian, (-π..π), ngược chiều kim đồng hồ là dương
    }

    public Vector2D multiply(float k) {
        return new Vector2D(this.x * k, this.y * k);
    }

    public Vector2D normalized() {
        float len = (float) Math.sqrt(this.x * this.x + this.y * this.y);
        if (len == 0) return new Vector2D(0, 0);
        return new Vector2D(this.x / len, this.y / len);
    }

    public float distancePointToLine(Vector2D point, Vector2D lineStart, Vector2D lineEnd) {
        Vector2D AB = lineEnd.subtracted(lineStart);
        Vector2D AP = point.subtracted(lineStart);

        float abLength = AB.length();
        if (abLength < Constants.COLLISION_EPSILON) {
            return AP.length();
        }

        return Math.abs(AB.cross(AP)) / abLength;
    }
}
