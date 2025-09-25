package core;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ResourceLoader {

    private static final String ASSETS_PATH = "assets/";
    private static final HashMap<String, BufferedImage> imageCache = new HashMap<>();
    private static final HashMap<String, Clip> soundCache = new HashMap<>();

    // 🔹 Load ảnh (BufferedImage) – giữ tên loadImage
    public static BufferedImage loadImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        try {
            BufferedImage img = ImageIO.read(new File(ASSETS_PATH + path));
            imageCache.put(path, img);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔹 Load ảnh thành ImageIcon (cho JLabel, JButton…)
    public static ImageIcon getImageIcon(String path) {
        BufferedImage img = loadImage(path);
        return (img != null) ? new ImageIcon(img) : null;
    }

    // 🔹 Load âm thanh (Clip)
    public static Clip loadSound(String path) {
        if (soundCache.containsKey(path)) {
            return soundCache.get(path);
        }
        try {
            File file = new File(ASSETS_PATH + path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            soundCache.put(path, clip);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔹 Quản lý nhạc nền
    private static Clip bgMusic;

    public static void playBackgroundMusic(String path) {
        stopBackgroundMusic();
        bgMusic = loadSound(path);
        if (bgMusic != null) {
            bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
            bgMusic.start();
        }
    }

    public static void stopBackgroundMusic() {
        if (bgMusic != null && bgMusic.isRunning()) {
            bgMusic.stop();
            bgMusic.close();
        }
    }

    // 🔹 Load font
    public static Font loadFont(String path, float size) {
        try {
            File fontFile = new File(ASSETS_PATH + path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, (int) size); // fallback
        }
    }
}
