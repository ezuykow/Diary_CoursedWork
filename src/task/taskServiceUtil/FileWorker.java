package task.taskServiceUtil;

import task.Task;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileWorker {

    private static final File TASKS_FILE = new File("src/task/taskServiceUtil/TaskStorage.bin");
    private static final File DELETED_TASKS_FILE = new File("src/task/taskServiceUtil/DeletedTaskStorage.bin");

    public static Map<Integer, Task> getTasksFromFile() {
        try (var ois = new ObjectInputStream(new FileInputStream(TASKS_FILE))){
            return (HashMap<Integer, Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    public static Map<Integer, Task> getDeletedTasksFromFile() {
        try (var ois = new ObjectInputStream(new FileInputStream(DELETED_TASKS_FILE))){
            return (HashMap<Integer, Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    public static void refreshFiles(Map<Integer, Task> tasks, Map<Integer, Task> deletedTasks) {
        refreshTasksFile(tasks);
        refreshDeletedTaskFile(deletedTasks);
    }

    public static void refreshTasksFile(Map<Integer, Task> map) {
        try (var oos = new ObjectOutputStream(new FileOutputStream(TASKS_FILE))) {
            oos.writeObject(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void refreshDeletedTaskFile(Map<Integer, Task> map) {
        try (var oos = new ObjectOutputStream(new FileOutputStream(DELETED_TASKS_FILE))) {
            oos.writeObject(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
