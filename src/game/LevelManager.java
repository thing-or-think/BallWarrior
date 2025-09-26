package game;

import entity.Brick;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LevelManager chịu trách nhiệm đọc file level (dạng text)
 * và sinh ra danh sách các viên gạch (Brick) để đưa vào game.
 */
public class LevelManager {

    private static final Logger LOGGER = Logger.getLogger(LevelManager.class.getName());

    private int brickWidth = 60;
    private int brickHeight = 20;
    private int startX = 50;
    private int startY = 50;
    private int gap = 5;

    /**
     * Đọc file level và tạo danh sách gạch.
     * - Mỗi dòng trong file đại diện cho một hàng gạch.
     * - Ký tự '1' => có gạch, ký tự khác => bỏ trống.
     *
     * @param path đường dẫn tới file level
     * @return danh sách các viên gạch (Brick)
     */
    public List<Brick> load(String path) {
        List<Brick> bricks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    char c = line.charAt(col);

                    if (c == '1') {
                        int x = startX + col * (brickWidth + gap);
                        int y = startY + row * (brickHeight + gap);
                        Color color = (row % 2 == 0) ? Color.RED : Color.ORANGE;
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, color));
                    }
                }
                row++;
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load level file: " + path, e);
        }

        return bricks;
    }
}
