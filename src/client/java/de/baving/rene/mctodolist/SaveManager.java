package de.baving.rene.mctodolist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private static final File SAVE_FILE = new File("config/mctodolist.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void saveTasks(List<String> tasks) {
        try (Writer writer = new FileWriter(SAVE_FILE)) {
            GSON.toJson(tasks, writer);
        } catch (IOException e) {
            System.err.println("Failed to save tasks: " + e.getMessage());
        }
    }

    public static List<String> loadTasks() {
        if (!SAVE_FILE.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new FileReader(SAVE_FILE)) {
            Type taskListType = new TypeToken<List<String>>() {}.getType();
            return GSON.fromJson(reader, taskListType);
        } catch (IOException e) {
            System.err.println("Failed to load tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}