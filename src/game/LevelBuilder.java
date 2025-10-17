package game;

import entity.Brick;
import utils.Constants;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {
    public static List<Brick> buildBricks(LevelData level) {
        List<Brick> bricks = new ArrayList<>();
        int brickSize = 50;
        // Tổng chiều rộng của dãy gạch
        int totalWidth = level.cols * brickSize;

        // Tính điểm bắt đầu để căn giữa + cách trục X 50
        int startX = (Constants.WIDTH - totalWidth) / 2;
        int startY = 50;

        for (int r = 0; r < level.rows; r++) {
            for (int c = 0; c < level.cols; c++) {
                Brick.Type type;
                switch (level.brickMap[r][c]) {
                    case 0: type = Brick.Type.BEDROCK; break;
                    case 1: type = Brick.Type.COBBLESTONE; break;
                    case 2: type = Brick.Type.GOLD; break;
                    default: type = Brick.Type.DIAMOND; break;
                }
                bricks.add(new Brick(
                        startX + c * (brickSize),
                        startY + r * (brickSize),
                        brickSize,
                        brickSize,
                        type
                ));

            }
        }
        return bricks;
    }
}
