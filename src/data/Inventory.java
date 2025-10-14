package data;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<SkinData> balls;
    private List<SkinData> paddles;

    public Inventory(Inventory inventory) {
        this.balls = new ArrayList<>(inventory.balls);
        this.paddles = new ArrayList<>(inventory.paddles);
    }

    public List<SkinData> getBalls() { return balls; }
    public List<SkinData> getPaddles() { return paddles; }

    public void setInventory(Inventory inventory) {
        this.balls = new ArrayList<>(inventory.balls);
        this.paddles = new ArrayList<>(inventory.paddles);
    }
}
