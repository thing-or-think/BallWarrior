package data;

import core.ResourceSaver;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerData {
    private String playerName;
    private final AtomicInteger coins;
    private final Equipped equipped;
    private final Inventory inventory;


    public PlayerData(PlayerData playerData) {
        this.playerName = playerData.playerName; // <-- THÊM DÒNG NÀY
        this.coins = new AtomicInteger(playerData.coins.get());
        this.equipped = new Equipped(playerData.equipped);
        this.inventory = new Inventory(playerData.inventory);

        PlayerData self = this; // giữ reference tới instance


    }

    public PlayerData() {
        this.playerName = "DefaultPlayer";
        this.coins = new AtomicInteger(0);
        this.equipped = null; // Sẽ được GSON nạp
        this.inventory = null; // Sẽ được GSON nạp
    }

    public AtomicInteger getCoins() { return coins; }
    public void setCoins(int coins) {
        this.coins.set(coins);
    }
    public Equipped getEquipped() { return equipped; }
    public Inventory getInventory() { return inventory; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
}
