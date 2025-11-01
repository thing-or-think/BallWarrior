package data;

public class LeaderboardEntry {
    public String playerName;
    public int score;

    // Constructor rỗng cho GSON
    public LeaderboardEntry() {}

    public LeaderboardEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }
}
