package game;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

/**
 * LevelManager
 * -------------------------------------------------------------------
 * Quản lý việc load 1 level từ file JSON.
 * (Phiên bản đơn giản)
 * -------------------------------------------------------------------
 */
public class LevelManager {
    private LevelData currentLevel;
    private final Gson gson = new Gson();

    /**
     * Load 1 level từ file JSON.
     */
    public void load(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            currentLevel = gson.fromJson(reader, LevelData.class);
            System.out.println("✅ Đã load level: " + currentLevel.name);
        } catch (IOException e) {
            System.err.println("❌ Lỗi đọc file: " + e.getMessage());
        }
    }

    public LevelData getCurrentLevel() {
        return currentLevel;
    }
}
