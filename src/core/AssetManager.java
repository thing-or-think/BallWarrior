package core;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private static final Map<String, BufferedImage> images = new HashMap<>();

    public static BufferedImage getImage(String key) {
        return images.computeIfAbsent(key,
                path -> ResourceLoader.loadImage(key));
    }

    public static void clear() {
        images.clear();
    }
}