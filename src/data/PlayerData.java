package data;

import core.ResourceSaver;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerData {
    private final AtomicInteger coins;
    private final Equipped equipped;
    private final Inventory inventory;


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
}