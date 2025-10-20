package data;

import core.ResourceSaver;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerData {
    private AtomicInteger coins;
    private Equipped equipped;
    private Inventory inventory;


    public PlayerData(PlayerData playerData) {
        this.coins = new AtomicInteger(playerData.coins.get());
        this.equipped = new Equipped(playerData.equipped);
        this.inventory = new Inventory(playerData.inventory);

        PlayerData self = this; // giữ reference tới instance
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ResourceSaver.savePlayerData(self);
            System.out.println("Player data saved on exit!");
        }));
    }

    public AtomicInteger getCoins() { return coins; }
    public void setCoins(int coins) {
        this.coins.set(coins);
    }
    public Equipped getEquipped() { return equipped; }
    public Inventory getInventory() { return inventory; }

    public void setEquippedBall(int ballId) {
        equipped.setBallId(ballId);
    }

    public void setEquippedPaddle(int paddleId) {
        equipped.setPaddleId(paddleId);
    }

    public void setPlayerData(PlayerData playerData) {
        this.coins.set(playerData.coins.get());
        equipped.setEquipped(playerData.equipped);
        inventory.setInventory(playerData.inventory);
    }
}