package data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {

    private List<SkinData> items;
    private transient List<SkinData> balls;
    private transient List<SkinData> paddles;

    public Inventory(Inventory inventory) {
        this.items = new ArrayList<>(inventory.getItems());
        this.balls = this.items.stream()
                .filter(item -> "ball".equals(item.getType()))
                .collect(Collectors.toList());
        this.paddles = this.items.stream()
                .filter(item -> "paddle".equals(item.getType()))
                .collect(Collectors.toList());
    }

    public List<SkinData> getItems() { return items; }
    public List<SkinData> getBalls() { return balls; }
    public List<SkinData> getPaddles() { return paddles; }

    public void setInventory(Inventory inventory) {
        this.balls = new ArrayList<>(inventory.getBalls());
        this.paddles = new ArrayList<>(inventory.getPaddles());
    }
}
