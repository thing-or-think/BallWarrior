package game;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * LevelData
 * -------------------------------------------------------------------
 * Dữ liệu cơ bản cho một màn chơi Arkanoid.
 * -------------------------------------------------------------------
 */
public class LevelData {
    public String name;
    // Tên level
    public int rows;             // Số hàng gạch
    public int cols;
    // Số cột gạch
    public int[][] brickMap;
    // Ma trận bản đồ gạch (0 = trống, 1 = gạch)

    // --- CÁC TRƯỜNG MỚI ĐỂ PORT ---
    public String previewImagePath;
    public String backgroundPath;
    public transient String filePath;
    public transient BufferedImage previewImage;
    public transient ImageIcon background;
    public int index;
    public int startingMana;
    // --- HẾT TRƯỜNG MỚI ---


    // ==== CONSTRUCTOR ========================================================
    public LevelData(String name, int rows, int cols, int[][] brickMap) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.brickMap = brickMap;
    }

    // Constructor trống (cần khi dùng Gson)
    public LevelData() {}

    // --- CÁC PHƯƠNG THỨC MỚI ---
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setPreviewImage(BufferedImage previewImage) {
        this.previewImage = previewImage;
    }

    public void setBackground(ImageIcon background) {
        this.background = background;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    // --- HẾT PHƯƠNG THỨC MỚI ---

    @Override
    public String toString() {
        return "LevelData{name='" + name + "', " + rows + "x" + cols + "}";
    }
}
