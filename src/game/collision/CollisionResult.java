package game.collision;

import entity.Entity;
import utils.Vector2D;

public class CollisionResult {

    private Entity entity;                 // Đối tượng va chạm
    private Vector2D hitPoint;             // Điểm va chạm
    private Vector2D normal;               // Pháp tuyến tại va chạm
    private float time;                       // Thời gian va chạm trong frame (0–1)
    private Vector2D reflectedVelocity;    // Vận tốc sau khi phản xạ

    public CollisionResult(Entity entity, Vector2D hitPoint, Vector2D normal, float time, Vector2D reflectedVelocity) {
        this.entity = entity;
        this.hitPoint = hitPoint;
        this.normal = normal;
        this.time = time;
        this.reflectedVelocity = reflectedVelocity;
    }

    // Getter & Setter
    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Vector2D getHitPoint() {
        return new Vector2D(hitPoint);
    }

    public void setHitPoint(Vector2D hitPoint) {
        this.hitPoint = hitPoint;
    }

    public Vector2D getNormal() {
        return new Vector2D(normal);
    }

    public void setNormal(Vector2D normal) {
        this.normal = normal;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public Vector2D getReflectedVelocity() {
        return new Vector2D(reflectedVelocity);
    }

    public void setReflectedVelocity(Vector2D reflectedVelocity) {
        this.reflectedVelocity = reflectedVelocity;
    }

    // Kết quả có hợp lệ?
    public boolean isValid() {
        return entity != null && normal != null && time >= 0.0f && time <= 1.0f;
    }
}
