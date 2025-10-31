package core;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioService {

    // Lưu trữ Clip đã tải để tránh tải lại nhiều lần (caching)
    private static final Map<String, Clip> cache = new HashMap<>();

    /** * Tải file âm thanh vào bộ nhớ đệm.
     * @param filePath Đường dẫn đến file âm thanh (nên là .wav)
     */
    private static Clip loadClip(String filePath) {
        if (cache.containsKey(filePath)) {
            return cache.get(filePath);
        }
        try {
            File file = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            cache.put(filePath, clip);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("❌ Lỗi tải âm thanh: " + filePath);
            e.printStackTrace();
            return null;
        }
    }

    /** * Phát hiệu ứng âm thanh một lần.
     * Thích hợp cho click, pop, hit, v.v.
     */
    public static void playSound(String filePath) {
        Clip clip = loadClip(filePath);
        if (clip != null) {
            clip.stop(); // Dừng nếu đang phát
            clip.setFramePosition(0); // Tua về đầu
            clip.start();
        }
    }

    /** * Phát nhạc nền lặp lại.
     * @param filePath Đường dẫn đến file nhạc nền.
     */
    public static void loopMusic(String filePath) {
        Clip clip = loadClip(filePath);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Phát và lặp lại
        }
    }

    /** * Dừng âm thanh cụ thể.
     */
    public static void stopSound(String filePath) {
        if (cache.containsKey(filePath)) {
            cache.get(filePath).stop();
        }
    }
}
