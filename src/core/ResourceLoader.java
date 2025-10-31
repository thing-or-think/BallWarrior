package core;

import com.google.gson.Gson;
import data.PlayerData;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.HashMap;
import javax.sound.sampled.*;

public class ResourceLoader {

    private static final String DATA_PATH = "assets/data/playerData.json";
    private static final String SOUND_PATH = "assets/sounds/";
    private static final HashMap<String, Clip> soundCache = new HashMap<>();

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

    // ⭐ PHƯƠNG THỨC TẢI VÀ CACHE ÂM THANH
    public static Clip loadSound(String path) {
        if (soundCache.containsKey(path)) {
            return soundCache.get(path);
        }
        try {
            // Sử dụng ClassLoader để đảm bảo nó hoạt động trong JAR file
            File file = new File(SOUND_PATH + path);

            // Lấy Audio Stream và mở Clip
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            soundCache.put(path, clip);
            return clip;
        } catch (Exception e) {
            System.err.println("❌ Lỗi tải âm thanh: " + path);
            e.printStackTrace();
            return null;
        }
    }

    // ⭐ PHƯƠNG THỨC CHƠI NHẠC NỀN (Tùy chọn)
    private static Clip bgMusic;

    public static void playBackgroundMusic(String path) {
        stopBackgroundMusic();
        bgMusic = loadSound(path);
        if (bgMusic != null) {
            // Vòng lặp liên tục
            bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
            bgMusic.start();
        }
    }

    public static void stopBackgroundMusic() {
        if (bgMusic != null && bgMusic.isRunning()) {
            bgMusic.stop();
            bgMusic.close();
            bgMusic = null;
        }
    }
}
