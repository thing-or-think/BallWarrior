package core;

import com.google.gson.Gson;
import data.PlayerData;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class ResourceLoader {

    private static final String DATA_PATH = "assets/data/playerData.json";

    public static PlayerData loadPlayerData() {
        try (FileReader reader = new FileReader(DATA_PATH)) {
            // Đọc JSON thành đối tượng PlayerData
            return new Gson().fromJson(reader, PlayerData.class);

        } catch (FileNotFoundException e) {
            // Lỗi file không tồn tại → có thể tạo file mặc định hoặc báo lỗi
            System.err.println("Không tìm thấy file dữ liệu: " + DATA_PATH);
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            // Lỗi đọc file
            System.err.println("Lỗi khi đọc file dữ liệu!");
            e.printStackTrace();
            return null;

        } catch (Exception e) {
            // Bắt các lỗi còn lại (JSON sai format, null,...)
            System.err.println("Lỗi không xác định khi tải dữ liệu người chơi!");
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage loadImage(String path) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Không tìm thấy ảnh, thì phải..PHẢI CHỊU.");
            img = null;
        }
        return img;
    }
}
