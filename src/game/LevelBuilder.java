package game;

import entity.Brick;
import utils.Constants;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {
    public static List<Brick> buildBricks(LevelData level) {
        List<Brick> bricks = new ArrayList<>();
        int brickWidth = Constants.WIDTH / level.cols;
        int brickHeight = 25;
        int brickSize = 40;

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
                        50 + c * (brickSize + 5),
                        50 + r * (brickSize + 5),
                        brickSize,
                        brickSize,
                        type
                ));

            }
        }
        return bricks;
    }
}
