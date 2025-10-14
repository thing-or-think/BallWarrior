package data;

public class Equipped {
    private int ballId;
    private int paddleId;

    public Equipped(Equipped equipped) {
        this.ballId = equipped.ballId;
        this.paddleId = equipped.paddleId;
    }

    public int getBallId() { return ballId; }
    public int getPaddleId() { return paddleId; }

    public void setEquipped(Equipped equipped) {
        this.ballId = equipped.ballId;
        this.paddleId = equipped.paddleId;
    }
}
