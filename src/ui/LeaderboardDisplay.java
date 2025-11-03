package ui;

import data.GameData;
import data.LeaderboardEntry;
import utils.Constants;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderboardDisplay {

    private List<LeaderboardEntry> sortedScores;
    private String levelName;
    private boolean isVisible = true;

    private static final int MAX_ENTRIES_TO_SHOW = 5; // Chỉ hiện Top 5

    public LeaderboardDisplay() {
        this.sortedScores = Collections.emptyList();
        this.levelName = "Leaderboard";
    }

    /**
     * Cập nhật dữ liệu leaderboard cho màn chơi hiện tại
     */
    public void updateData(String levelId, GameData gameData, String levelName) {
        this.levelName = levelName;
        List<LeaderboardEntry> scores = gameData.getLeaderboards().get(levelId);

        if (scores == null || scores.isEmpty()) {
            this.sortedScores = Collections.emptyList();
        } else {
            // Sắp xếp giảm dần và lấy 5 người cao nhất
            this.sortedScores = scores.stream()
                    .sorted(Comparator.comparingInt(e -> -e.score)) // Sắp xếp giảm dần
                    .limit(MAX_ENTRIES_TO_SHOW)
                    .collect(Collectors.toList());
        }
    }

    public void toggleVisibility() {
        this.isVisible = !this.isVisible;
    }

    public void draw(Graphics2D g) {
        if (!isVisible) return;

        int boxWidth = 220;
        int boxHeight = 200;
        int boxX = Constants.WINDOW_WIDTH - boxWidth - 15; // 30 padding
        int boxY = (Constants.WINDOW_HEIGHT - boxHeight) / 2 - 50; // Đặt ở giữa, hơi cao

        // Vẽ nền mờ
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

        // Vẽ viền
        g.setColor(Color.CYAN);
        g.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

        // Vẽ tiêu đề
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(levelName, boxX + (boxWidth - fm.stringWidth(levelName)) / 2, boxY + 25);
        g.drawString("Top 5 Scores", boxX + (boxWidth - fm.stringWidth("Top 5 Scores")) / 2, boxY + 45);

        // Vẽ danh sách điểm
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        fm = g.getFontMetrics();
        int yPos = boxY + 75;

        if (sortedScores.isEmpty()) {
            g.setColor(Color.GRAY);
            g.drawString("No scores yet.", boxX + 20, yPos);
        } else {
            for (int i = 0; i < sortedScores.size(); i++) {
                LeaderboardEntry entry = sortedScores.get(i);
                String rank = (i + 1) + ". ";
                String name = entry.playerName.length() > 10 ? entry.playerName.substring(0, 7) + "..." : entry.playerName;
                String score = String.valueOf(entry.score);

                g.setColor(Color.WHITE);
                g.drawString(rank + name, boxX + 20, yPos);

                g.setColor(Color.YELLOW);
                g.drawString(score, boxX + boxWidth - fm.stringWidth(score) - 20, yPos);

                yPos += 22;
            }
        }
    }
}
