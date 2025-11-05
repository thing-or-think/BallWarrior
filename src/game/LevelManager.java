package game;

import com.google.gson.Gson;
import core.ResourceLoader; // Thêm import này

import java.awt.image.BufferedImage; // Thêm import này
import java.io.File; // Thêm import này
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; // Thêm import này
import java.util.List; // Thêm import này

/**
 * LevelManager
 * -------------------------------------------------------------------
 * Quản lý việc load level từ file JSON.
 * -------------------------------------------------------------------
 */
public class LevelManager {
    private LevelData currentLevel;
    private final List<LevelData> allLevels = new ArrayList<>(); // Thêm dòng này
    private final Gson gson = new Gson();

    /**
     * [MỚI] Quét thư mục và tải tất cả các file level .json
     */
    public void loadAllLevels(String directoryPath) {
        allLevels.clear();
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try (FileReader reader = new FileReader(file)) {
                    LevelData level = gson.fromJson(reader, LevelData.class);
                    if (level != null) {
                        level.setFilePath(file.getPath());
                        if (level.previewImagePath != null && !level.previewImagePath.isEmpty()) {
                            // Dùng ResourceLoader của merge_feature
                            String imagePath = "assets/previews/" + level.previewImagePath;
                            BufferedImage img = ResourceLoader.loadImage(imagePath);
                            level.setPreviewImage(img);
                        }
                        allLevels.add(level);
                    }
                } catch (IOException e) {
                    System.err.println("Lỗi đọc file level: " + file.getPath() + " - " + e.getMessage());
                }
            }
        }
        System.out.println("Đã tìm thấy và tải " + allLevels.size() + " màn chơi.");
    }

    /**
     * [MỚI] Trả về danh sách tất cả level đã tải
     */
    public List<LevelData> getAllLevels() {
        return allLevels;
    }

    /**
     * Load 1 level cụ thể từ file JSON.
     * (Giữ lại phương thức này để GameWorld sử dụng)
     */
    public LevelData load(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            currentLevel = gson.fromJson(reader, LevelData.class);
            if (currentLevel != null) {
                System.out.println("✅ Đã load level: " + currentLevel.name);
                if (currentLevel.backgroundPath != null && !currentLevel.backgroundPath.isEmpty()) {
                    String imagePath = "assets/images/Bg/" + currentLevel.backgroundPath;
                    BufferedImage img = ResourceLoader.loadImage(imagePath);
                    currentLevel.setBackground(img);
                    if (img == null) {
                        System.err.println("CẢNH BÁO: Không tìm thấy ảnh nền: " + imagePath);
                    }
                }
            }
            return currentLevel;
        } catch (IOException e) {
            System.err.println("❌ Lỗi đọc file: " + e.getMessage());
            currentLevel = null;
            return null;
        }
    }

    public LevelData getCurrentLevel() {
        return currentLevel;
    }
}
