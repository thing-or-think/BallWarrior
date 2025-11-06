package core;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioService {

    private static final String SOUND_PATH = "assets/sounds/";
    // Lưu trữ Clip đã tải để tránh tải lại nhiều lần (caching)
    private static final Map<String, Clip> cache = new HashMap<>();

    /** * Tải file âm thanh vào bộ nhớ đệm.
     * @param filePath Đường dẫn đến file âm thanh
     */
    private static Clip loadClip(String filePath) {
        if (cache.containsKey(filePath)) {
            return cache.get(filePath);
        }
        try {
            File file = new File(SOUND_PATH+filePath);

            // Lấy Audio Stream và mở Clip
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            cache.put(filePath, clip);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Lỗi tải âm thanh: " + filePath);
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

    /** * Phát nhạc nền lặp lại, giảm âm lượng 10 dB.
     * @param fileName Tên file nhạc nền.
     */
    public static void loopMusic(String fileName) {
        Clip clip = loadClip(fileName);
        if (clip != null) {
            // GIẢM 10 dB
            final float VOLUME_ADJUSTMENT_DB = -10.0f;

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // Đặt giá trị Gain mới
                float minGain = gainControl.getMinimum();
                float newGain = VOLUME_ADJUSTMENT_DB;

                // Đảm bảo giá trị không nhỏ hơn ngưỡng min cho phép
                gainControl.setValue(Math.max(minGain, newGain));
            }

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
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
