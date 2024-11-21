package de.baving.rene.mctodolist;

import java.util.List;

public class TaskManager {
    private final List<String> tasks;
    private static TaskManager instance;

    private TaskManager() {
        tasks = SaveManager.loadTasks();
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public void addTask(String task) {
        tasks.add(task);
        SaveManager.saveTasks(tasks);
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            SaveManager.saveTasks(tasks);
        }
    }

    public List<String> getTasks() {
        return tasks;
    }
}
