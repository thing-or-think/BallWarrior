package data;

import java.util.concurrent.atomic.AtomicInteger;

public class Equipped {
    private AtomicInteger ballId;
    private AtomicInteger paddleId;

    public Equipped(Equipped equipped) {
        this.ballId = new AtomicInteger(equipped.ballId.get());
        this.paddleId = new AtomicInteger(equipped.paddleId.get());
    }

    public int getBallId() { return ballId.get(); }
    public int getPaddleId() { return paddleId.get(); }
    public AtomicInteger getBallIdRef() { return ballId; }
    public AtomicInteger getPaddleIdRef() { return paddleId; }

    public void setEquipped(Equipped equipped) {
        this.ballId = equipped.ballId;
        this.paddleId = equipped.paddleId;
    }

    public void setBallId(int ballId) {
        this.ballId.set(ballId);
    }

    public void setPaddleId(int paddleId) {
        this.paddleId.set(paddleId);
    }
}
