package core;

import com.google.gson.Gson;
import data.GameData; // THÊM
import data.PlayerData;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class ResourceLoader {


    private static final String GAME_DATA_PATH = "assets/data/GameData.json";
    private static final String PLAYER_TEMPLATE_PATH = "assets/data/playerData.json";

    public static PlayerData loadPlayerDataTemplate() {
        try (FileReader reader = new FileReader(PLAYER_TEMPLATE_PATH)) {
            // Đọc JSON thành đối tượng PlayerData
            return new Gson().fromJson(reader, PlayerData.class);
        } catch (FileNotFoundException e) {
            System.err.println("Không tìm thấy file mẫu: " + PLAYER_TEMPLATE_PATH);
            e.printStackTrace();
            return null; // Trả về null nếu không có file template
        } catch (IOException e) {
            // Lỗi đọc file
            System.err.println("Lỗi khi đọc file mẫu!");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            // Bắt các lỗi còn lại
            System.err.println("Lỗi không xác định khi tải file mẫu!");
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức mới (từ bước trước) để tải GameData
    public static GameData loadGameData() {
        try (FileReader reader = new FileReader(GAME_DATA_PATH)) {
            GameData gameData = new Gson().fromJson(reader, GameData.class);
            if (gameData == null) { // File rỗng
                return new GameData();
            }
            return gameData;
        } catch (FileNotFoundException e) {
            System.err.println("Không tìm thấy GameData.json. Tạo file mới.");
            return new GameData(); // Trả về đối tượng rỗng
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file GameData!");
            e.printStackTrace();
            return new GameData();
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi tải GameData!");
            e.printStackTrace();
            return new GameData();
        }
    }

    public static BufferedImage loadImage(String path) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Không tìm thấy ảnh, thì phải..PHẢI CHỊU." + path);
            img = null;
        }
        return img;
    }
}
