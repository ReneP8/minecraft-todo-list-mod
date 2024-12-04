// SaveManager.java
package de.baving.rene.mctodolist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import de.baving.rene.mctodolist.data.Task;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveManager {
    private static final File SAVE_FILE = new File("config/mctodolist.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void saveTasks(List<Task> tasks) {
        try (Writer writer = new FileWriter(SAVE_FILE)) {
            GSON.toJson(tasks, writer);
        } catch (IOException e) {
            System.err.println("Failed to save tasks: " + e.getMessage());
        }
    }

    public static List<Task> loadTasks() {
        if (!SAVE_FILE.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new FileReader(SAVE_FILE)) {
            Type dataType = new TypeToken<List<Task>>() {}.getType();
            return GSON.fromJson(reader, dataType);
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Failed to load tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static Map<String, List<String>> createEmptyData() {
        Map<String, List<String>> data = new HashMap<>();
        data.put("tasks", new ArrayList<>());
        data.put("doneTasks", new ArrayList<>());
        return data;
    }
}