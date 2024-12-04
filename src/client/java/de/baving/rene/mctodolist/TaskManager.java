package de.baving.rene.mctodolist;

import de.baving.rene.mctodolist.data.Task;

import java.util.Collections;
import java.util.List;

public class TaskManager {
    private static TaskManager instance;
    private final List<Task> tasks;

    private TaskManager() {
        tasks = SaveManager.loadTasks();
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
        SaveManager.saveTasks(tasks);
    }

    public void removeTask(int index) {
        tasks.remove(index);
        SaveManager.saveTasks(tasks);
    }

    public void markTaskAsDone(int index) {
        Task task = tasks.get(index);
        task.setDone(true);
        SaveManager.saveTasks(tasks);
    }

    public void markTaskAsNotDone(int index) {
        Task task = tasks.get(index);
        task.setDone(false);
        SaveManager.saveTasks(tasks);
    }
}