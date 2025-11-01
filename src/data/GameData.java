package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameData {
    // Danh sách tất cả người chơi và đồ đạc của họ
    private List<PlayerData> allPlayers;

    // Key là LevelID (vd: "level1.json"), Value là danh sách điểm
    private Map<String, List<LeaderboardEntry>> leaderboards;

    // Tên của người chơi đang đăng nhập
    private String currentPlayerName;

    public GameData() {
        this.allPlayers = new ArrayList<>();
        this.leaderboards = new HashMap<>();
        this.currentPlayerName = null;
    }

    // --- Getters ---
    public List<PlayerData> getAllPlayers() { return allPlayers; }
    public Map<String, List<LeaderboardEntry>> getLeaderboards() { return leaderboards; }
    public String getCurrentPlayerName() { return currentPlayerName; }

    // --- Setters ---
    public void setCurrentPlayerName(String name) { this.currentPlayerName = name; }
}
