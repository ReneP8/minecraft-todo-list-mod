package de.baving.rene.mctodolist;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private static TaskManager instance;
    private final List<String> tasks;
    private final List<String> doneTasks;

    private TaskManager() {
        Map<String, List<String>> data = SaveManager.loadTasks();
        tasks = data.get("tasks");
        doneTasks = data.get("doneTasks");
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public List<String> getDoneTasks() {
        return doneTasks;
    }

    public void addTask(String task) {
        tasks.add(task);
        SaveManager.saveTasks(tasks, doneTasks);
    }

    public void removeTask(int index) {
        tasks.remove(index);
        SaveManager.saveTasks(tasks, doneTasks);
    }

    public void markTaskAsDone(int index) {
        String task = tasks.remove(index);
        doneTasks.add(task);
        SaveManager.saveTasks(tasks, doneTasks);
    }

    public void removeDoneTask(int index) {
        doneTasks.remove(index);
        SaveManager.saveTasks(tasks, doneTasks);
    }

    public void markTaskAsNotDone(int index) {
        String task = doneTasks.remove(index);
        tasks.add(task);
        SaveManager.saveTasks(tasks, doneTasks);
    }

    public void reorderTasks(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= tasks.size() || toIndex < 0 || toIndex >= tasks.size()) {
            return;
        }
        Collections.swap(tasks, fromIndex, toIndex);
        SaveManager.saveTasks(tasks, doneTasks);
    }
}