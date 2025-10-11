package game;

/**
 * LevelData
 * -------------------------------------------------------------------
 * Dữ liệu cơ bản cho một màn chơi Arkanoid.
 * -------------------------------------------------------------------
 */
public class LevelData {
    public String name;          // Tên level
    public int rows;             // Số hàng gạch
    public int cols;             // Số cột gạch
    public int[][] brickMap;     // Ma trận bản đồ gạch (0 = trống, 1 = gạch)

    // ==== CONSTRUCTOR ========================================================
    public LevelData(String name, int rows, int cols, int[][] brickMap) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.brickMap = brickMap;
    }

    // Constructor trống (cần khi dùng Gson)
    public LevelData() {}

    @Override
    public String toString() {
        return "LevelData{name='" + name + "', " + rows + "x" + cols + "}";
    }
}
