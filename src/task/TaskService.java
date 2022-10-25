package task;

import menu.Menu;
import task.taskServiceUtil.NewTask;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskService {

    private static int ID;

    private static Map<Integer, Task> tasks;
    private static Map<Integer, Task> deletedTasks;
    private static final File tasksFile;
    private static final File deletedTasksFile;

    static {
        tasksFile = new File("src/task/taskServiceUtil/TaskStorage.bin");
        try (var ois = new ObjectInputStream(new FileInputStream(tasksFile))){
            tasks = (HashMap<Integer, Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            tasks = new HashMap<>();
        }
        tasks.keySet().stream().max(Integer::compareTo).ifPresent(m -> ID = m);

        deletedTasksFile = new File("src/task/taskServiceUtil/DeletedTaskStorage.bin");
        try (var ois = new ObjectInputStream(new FileInputStream(deletedTasksFile))){
            deletedTasks = (HashMap<Integer, Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            deletedTasks = new HashMap<>();
        }
    }

    private TaskService() {}

    public static void createNewTask() {
        tasks.put(++ID, NewTask.getNewTask());
        refreshTasksFile();
        System.out.println("New task  added successfully!");
        Menu.show();
    }

    public static void deleteTask() {
        if (tasks.isEmpty()) {
            System.out.println("Tasks not found!");
            Menu.show();
        } else {
            int id = waitValidIdOrZero();
            if (id == 0) {
                Menu.show();
            } else {
                deletedTasks.put(id, tasks.get(id));
                tasks.remove(id);
                refreshDeletedTaskFile();
                refreshTasksFile();
                System.out.println("Task deleted successfully!");
                Menu.show();
            }
        }
    }

    private static int waitValidIdOrZero() {
        int id = -1;
        Scanner in = new Scanner(System.in);
        while (!tasks.containsKey(id)) {
            System.out.println("Select the desired task:");
            showIdsAndTitles(tasks);
            System.out.println("Enter 0 to return to the menu");
            id = in.nextInt();
            if (id == 0) return 0;
        }
        return id;
    }

    public static void getGroupedByDaysTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Tasks not found!");
        } else {
            Set<LocalDate> ld = getUniqueDates();
            for (LocalDate date : ld) {
                System.out.println("Tasks for " + date + ":");
                showTasksByDay(date);
            }
        }
        showInfo(tasks);
    }

    private static Set<LocalDate> getUniqueDates() {
        Set<LocalDate> dates = new TreeSet<>();
        for (Map.Entry<Integer, Task> e : tasks.entrySet()) {
            LocalDate ld = e.getValue().getTime().toLocalDate();
            dates.add(ld);
        }
        return dates;
    }

    public static void getTasksByDay(){
        String day = "";
        Scanner in = new Scanner(System.in);
        while (!day.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            System.out.println("Enter a date (format DD.MM.YYYY) or 0 to return:");
            day = in.nextLine();
            if (day.equals("0")) {
                Menu.show();
                return;
            }
        }
        LocalDate ld = parseDate(day);
        showTasksByDay(ld);
        showInfo(tasks);
    }

    private static void showTasksByDay(LocalDate ld) {
        boolean isEmpty = true;
        for (Map.Entry<Integer, Task> e : tasks.entrySet()) {
            if (e.getValue().getTime().toLocalDate().equals(ld)) {
                isEmpty = false;
                System.out.println(
                        "   " + e.getKey() + ". " + e.getValue().getTitle()
                );
            }
        }
        if (isEmpty) System.out.println("Tasks not found!");
    }

    private static LocalDate parseDate(String s) {
        String[] parts = s.split("\\.");
        return LocalDate.of(Integer.parseInt(parts[2]),
                Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
    }

    public static void showDeletedTasks() {
        if (deletedTasks.isEmpty()) {
            System.out.println("Tasks not found!");
            Menu.show();
        } else {
            showIdsAndTitles(deletedTasks);
            showInfo(deletedTasks);
        }
    }

    public static void dropDeleted() {
        deletedTasks = new HashMap<>();
        refreshDeletedTaskFile();
        System.out.println("Drop successful!");
        Menu.show();
    }

    public static void showAll() {
        if (tasks.isEmpty()) {
            System.out.println("Tasks not found!");
            Menu.show();
        } else {
            showIdsAndTitles(tasks);
            showInfo(tasks);
        }
    }

    private static void showIdsAndTitles(Map<Integer, Task> map) {
        map.forEach((k, v) -> System.out.println(
                "   " + k + ". " + v.getTitle() + ", " + v.getTime()
        ));
    }

    private static void showInfo(Map<Integer, Task> map) {
        Scanner in = new Scanner(System.in);
        int id = -1;
        while (!map.containsKey(id)) {
            System.out.println("Enter a 0 to return to menu or id to show info of task:");
            id = in.nextInt();
            if (id == 0) {
                Menu.show();
                return;
            }
        }
        showInfoById(id, map);
    }

    private static void showInfoById(int id, Map<Integer, Task> map) {
        Task task = map.get(id);
        System.out.println(task);
        editOrReturn(id, map);
    }

    private static void editOrReturn(int id, Map<Integer, Task> map) {
        System.out.println("-------------------------------------");
        Scanner in = new Scanner(System.in);
        int choose = -1;
        while (choose < 0 || choose > 2) {
            System.out.println("Enter a 0 to return, 1 to edit title or 2 to edit description:");
            choose = in.nextInt();
        }
        switch (choose) {
            case 0:
                Menu.show();
                return;
            case 1:
                editTitle(id, map);
                return;
            case 2:
                editDescription(id, map);
        }
    }

    private static void editTitle(int id, Map<Integer, Task> map) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a new title:");
        String newTitle = in.nextLine();
        map.get(id).setTitle(newTitle);
        refreshTasksFile();
        refreshDeletedTaskFile();
        showInfoById(id, map);
    }

    private static void editDescription(int id, Map<Integer, Task> map) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a new description:");
        String newDesc = in.nextLine();
        map.get(id).setDescription(newDesc);
        refreshTasksFile();
        refreshDeletedTaskFile();
        showInfoById(id, map);
    }

    private static void refreshTasksFile() {
        try (var oos = new ObjectOutputStream(new FileOutputStream(tasksFile))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void refreshDeletedTaskFile() {
        try (var oos = new ObjectOutputStream(new FileOutputStream(deletedTasksFile))) {
            oos.writeObject(deletedTasks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
