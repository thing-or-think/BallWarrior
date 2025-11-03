package game;

import com.google.gson.Gson;
import core.ResourceLoader;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * LevelManager
 * -------------------------------------------------------------------
 * Quản lý việc load 1 level từ file JSON.
 * (Phiên bản đơn giản)
 * -------------------------------------------------------------------
 */
public class LevelManager {
    private final List<LevelData> allLevels = new ArrayList<>();
    private final Gson gson = new Gson();

    /**
     * Quét thư mục và tải tất cả các file level .json
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

    public List<LevelData> getAllLevels() {
        return allLevels;
    }

    /**
     * Tải một level cụ thể theo đường dẫn.
     * Phương thức này vẫn hữu ích cho GameWorld.
     */

    public LevelData loadLevelByPath(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            LevelData level = gson.fromJson(reader, LevelData.class);
            System.out.println("Đã tải level: " + level.name);
            return level;
        } catch (IOException e) {
            System.err.println("Lỗi đọc file: " + e.getMessage());
            return null;
        }
    }

}
