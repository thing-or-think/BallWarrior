package core;

import entity.Rarity;
import entity.Skins;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ResourceLoader {
    public static BufferedImage loadImg (String path) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
//            System.out.println("Không tìm thấy ảnh, thì phải..PHẢI CHỊU.");
            img = null;
        }
        return img;
    }
    /** Đọc file lấy skins */
    public static List<Skins> loadSkins(String filePath) {
        List<Skins> skins = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(",");
                if (parts.length < 7) continue;
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                boolean hasImage = Boolean.parseBoolean(parts[2].trim());
                Rarity rarity = Rarity.valueOf(parts[3].trim().toUpperCase());
                int price = Integer.parseInt(parts[4].trim());
                boolean isBought = Boolean.parseBoolean(parts[5].trim());
                String last = parts[6].trim();
                if (hasImage) {
                    skins.add(new Skins(id, name, rarity, price, isBought, last));
                } else {
                    Color color = parseColor(last);
                    if (color == null) color = Color.WHITE;
                    skins.add(new Skins(id, name, rarity, price, isBought, color));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy file Skins tại: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Lỗi I/O khi đọc file Skins.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi định dạng dữ liệu (ID, Price, Rarity, Boolean) trong file Skins.");
            e.printStackTrace();
        }
        return skins;
    }

    /** Ghi file skins: Đọc, tìm ID, cập nhật trạng thái isBought, và ghi đè lại file. */
    public static void updateIsBought(String filePath, int id) {
        List<String> lines = new ArrayList<>();
        boolean idFound = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String originalLine = line;
                String trimmed = originalLine.trim();
                if (trimmed.startsWith("#") || trimmed.isEmpty()) {
                    lines.add(originalLine);
                    continue;
                }
                String[] rawParts = trimmed.split(",");
                if (rawParts.length < 7) {
                    lines.add(originalLine);
                    continue;
                }
                String[] parts = new String[rawParts.length];
                for (int i = 0; i < rawParts.length; i++) {
                    parts[i] = rawParts[i].trim();
                }
                try {
                    int currentId = Integer.parseInt(parts[0]);
                    if (currentId == id) {
                        parts[5] = "true";
                        idFound = true;
                        line = String.join(",", parts); // CHUẨN HÓA ĐỊNH DẠNG GHI
                        System.out.println("-> Dòng ID " + id + " mới được ghi: " + line);
                    } else {
                        line = originalLine;
                    }
                } catch (NumberFormatException nfe) {
                    line = originalLine;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("❌ LỖI ĐỌC FILE: Không thể đọc file để cập nhật: " + filePath);
            e.printStackTrace();
            return;
        }
        if (!idFound) {
            System.err.println("❌ Cảnh báo: Không tìm thấy Skin có ID = " + id + " để cập nhật. Không ghi file.");
            return;
        }
        // --- 2. GHI ĐÈ TOÀN BỘ FILE BẰNG DỮ LIỆU MỚI (Dùng NIO) ---
        try {
            Files.write(
                    Paths.get(filePath),
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            System.out.println("✅ Đã ghi thành công trạng thái skin mới vào file.");
        } catch (IOException e) {
            System.err.println("❌ LỖI GHI FILE: Vui lòng kiểm tra quyền truy cập hoặc đường dẫn: " + new File(filePath).getAbsolutePath());
            e.printStackTrace();
        }
    }

    /** Lấy số tiền hiện tại từ file */
    public static int getMoney(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") && !line.contains(",")) {
                    line = line.replace("#", "").trim();
                    return Integer.parseInt(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Ghi lại số tiền mới vào file */
    public static void setMoney(String filePath, int newMoney) {
        try {
            List<String> lines = new ArrayList<>();
            boolean moneyUpdated = false;
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!moneyUpdated && line.trim().startsWith("#") && !line.contains(",")) {
                        line = "#" + newMoney;
                        moneyUpdated = true;
                    }
                    lines.add(line);
                }
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
                for (String l : lines) pw.println(l);
            }
            System.out.println("✅Đã cập nhật số tiền mới: " + newMoney);

        } catch (Exception e) {
            System.err.println("❌Lỗi khi cập nhật số tiền: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Lấy ID của skin Ball đang được trang bị từ file */
    public static int getEquippedBallId(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#EQUIPPED_BALL_ID:")) {
                    String idStr = line.replace("#EQUIPPED_BALL_ID:", "").trim();
                    return Integer.parseInt(idStr);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi đọc ID Ball trang bị: " + e.getMessage());
        }
        return 0; // Trả về ID 0 nếu không tìm thấy
    }

    /** Ghi lại ID của skin Ball đang được trang bị vào file */
    public static void setEquippedBallId(String filePath, int equippedId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            List<String> newLines = new ArrayList<>();
            boolean updated = false;
            for (String line : lines) {
                String trimmed = line.trim();
                if (!updated && trimmed.startsWith("#EQUIPPED_BALL_ID:")) {
                    newLines.add("#EQUIPPED_BALL_ID:" + equippedId);
                    updated = true;
                } else {
                    newLines.add(line);
                }
            }
            Files.write(Paths.get(filePath), newLines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("✅ Đã lưu ID Ball trang bị: " + equippedId);
        } catch (IOException e) {
            System.err.println("❌ Lỗi I/O khi ghi ID Ball trang bị: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /** Lấy ID của skin Paddle đang được trang bị từ file */
    public static int getEquippedPaddleId(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#EQUIPPED_PADDLE_ID:")) {
                    String idStr = line.replace("#EQUIPPED_PADDLE_ID:", "").trim();
                    return Integer.parseInt(idStr);
                }
            }
        } catch (Exception e) {
            System.err.println("❌Lỗi đọc ID Paddle trang bị: " + e.getMessage());
        }
        return 0; // Mặc định ID 0
    }
    /** Ghi lại ID của skin Paddle đang được trang bị vào file */
    public static void setEquippedPaddleId(String filePath, int equippedId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            List<String> newLines = new ArrayList<>();
            boolean updated = false;
            for (String line : lines) {
                String trimmed = line.trim();
                if (!updated && trimmed.startsWith("#EQUIPPED_PADDLE_ID:")) {
                    newLines.add("#EQUIPPED_PADDLE_ID:" + equippedId);
                    updated = true;
                } else {
                    newLines.add(line);
                }
            }
            Files.write(Paths.get(filePath), newLines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("✅ Đã lưu ID Paddle trang bị: " + equippedId);
        } catch (IOException e) {
            System.err.println("❌ Lỗi I/O khi ghi ID Paddle trang bị: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Color parseColor(String s) {
        return switch (s.toUpperCase()) {
            case "RED" -> Color.RED;
            case "BLUE" -> Color.BLUE;
            case "GREEN" -> Color.GREEN;
            case "YELLOW" -> Color.YELLOW;
            case "CYAN" -> Color.CYAN;
            case "MAGENTA" -> Color.MAGENTA;
            case "PINK" -> Color.PINK;
            case "BLACK" -> Color.BLACK;
            case "ORANGE" -> Color.ORANGE;
            default -> null;
        };
    }
}
