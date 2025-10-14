package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.PlayerData;

import java.io.FileWriter;

public class ResourceSaver {
    private static final String DATA_PATH = "assets/data/playerData.json";

    public static void savePlayerData(PlayerData playerData) {
        try (FileWriter writer = new FileWriter(DATA_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(playerData, writer);
            System.out.println("Đã lưu playerData.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
