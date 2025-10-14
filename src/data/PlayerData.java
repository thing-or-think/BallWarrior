package data;

public class PlayerData {
    private int coins;
    private Equipped equipped;
    private Inventory inventory;


    public PlayerData(PlayerData playerData) {
        this.coins = playerData.coins;
        equipped = new Equipped(playerData.equipped);
        inventory = new Inventory(playerData.inventory);
    }

    public int getCoins() { return coins; }
    public void setCoins(int coins) {
        this.coins = coins;
    }
    public Equipped getEquipped() { return equipped; }
    public Inventory getInventory() { return inventory; }

    public void setPlayerData(PlayerData playerData) {
        this.coins = playerData.coins;
        equipped.setEquipped(playerData.equipped);
        inventory.setInventory(playerData.inventory);
    }
}
