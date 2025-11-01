package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.PlayerData;
import data.GameData;

import java.io.FileWriter;

public class ResourceSaver {
    private static final String DATA_PATH = "assets/data/GameData.json";

    public static void saveGameData(GameData gameData) {
        try (FileWriter writer = new FileWriter(DATA_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(gameData, writer);
            System.out.println("Đã lưu GameData.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
