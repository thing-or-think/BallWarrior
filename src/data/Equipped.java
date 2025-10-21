package data;

import java.util.concurrent.atomic.AtomicInteger;

public class Equipped {
    private final AtomicInteger ballId;
    private final AtomicInteger paddleId;
    private transient SkinData ballSkin;
    private transient SkinData paddleSkin;

    public Equipped(Equipped equipped) {
        this.ballId = new AtomicInteger(equipped.ballId.get());
        this.paddleId = new AtomicInteger(equipped.paddleId.get());
    }

    public AtomicInteger getBallIdRef() { return ballId; }
    public AtomicInteger getPaddleIdRef() { return paddleId; }
}
