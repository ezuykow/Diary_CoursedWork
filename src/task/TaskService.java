package task;

import menu.Menu;
import task.byKind.RepeatType;
import task.taskServiceUtil.ConsoleWorker;
import task.taskServiceUtil.FileWorker;
import task.taskServiceUtil.NewTask;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TaskService {

    private static int task_id;

    private static final Map<Integer, Task> tasks;
    private static Map<Integer, Task> deletedTasks;

    static {
        tasks = FileWorker.getTasksFromFile();
        tasks.keySet().stream().max(Integer::compareTo).ifPresent(m -> task_id = m);
        deletedTasks = FileWorker.getDeletedTasksFromFile();
    }

    private TaskService() {}

    public static void createNewTask() {
        tasks.put(++task_id, NewTask.getNewTask());
        FileWorker.refreshTasksFile(tasks);
        System.out.println("New task  added successfully!");
        Menu.show();
    }

    public static void deleteTask() {
        if (isTasksListNotEmpty(tasks)) {
            int id = ConsoleWorker.waitValidIdOrZeroForDelete(tasks);
            if (id != 0) {
                deletedTasks.put(id, tasks.remove(id));
                FileWorker.refreshFiles(tasks, deletedTasks);
                System.out.println("Task deleted successfully!");
            }
        }
        Menu.show();
    }

    private static boolean isTasksListNotEmpty(Map<Integer, Task> map) {
        if (map.isEmpty()) {
            System.out.println("Tasks not found!");
            return false;
        }
        return true;
    }

    public static void getGroupedByDaysTasks() {
        if (isTasksListNotEmpty(tasks)) {
            Set<LocalDate> ld = getUniqueDates();
            for (LocalDate date : ld) {
                System.out.println("Tasks for " + date + ":");
                ConsoleWorker.showIdsAndTitlesForDay(tasks, date);
            }
            showInfo(tasks);
        } else {
            Menu.show();
        }
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
        String day = ConsoleWorker.waitValidDateOrZero();
        if (day.equals("0")) {
            Menu.show();
            return;
        }
        LocalDate ld = LocalDate.parse(day, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        showTasksByDay(ld);
        showInfo(tasks);
    }

    private static void showTasksByDay(LocalDate ld) {
        Map<Integer, Task> map = findTasksForDay(ld);
        if (isTasksListNotEmpty(map)) {
            System.out.println("Tasks for " + ld + ":");
            ConsoleWorker.showIdsAndTitles(map);
            showInfo(map);
        } else {
            Menu.show();
        }
    }

    private static Map<Integer, Task> findTasksForDay(LocalDate ld) {
        Map<Integer, Task> map = new HashMap<>();
        for (Map.Entry<Integer, Task> e : tasks.entrySet()) {
            if (checkTaskForDay(e.getValue(), ld)) {
                map.put(e.getKey(), e.getValue());
            }
        }
        return map;
    }

    private static boolean checkTaskForDay(Task task, LocalDate ld) {
        LocalDate taskDate = task.getTime().toLocalDate();
        if (taskDate.isAfter(ld)) return false;
        while (taskDate.isBefore(ld) || taskDate.equals(ld)) {
            if (taskDate.equals(ld)) return true;
            if (task.getRepeatType().equals(RepeatType.SINGLE)) return false;
            taskDate = getNextDate(task, taskDate);
        }
        return false;
    }

    private static LocalDate getNextDate(Task task, LocalDate ld) {
        switch (task.getRepeatType()) {
            case DAILY:
                return ld.plusDays(1);
            case WEEKLY:
                return ld.plusWeeks(1);
            case MONTHLY:
                return ld.plusMonths(1);
            case ANNUAL:
                return ld.plusYears(1);
            default:
                return ld;
        }
    }

    public static void showDeletedTasks() {
        if (isTasksListNotEmpty(deletedTasks)) {
            ConsoleWorker.showIdsAndTitles(deletedTasks);
            showInfo(deletedTasks);
            return;
        }
        Menu.show();
    }

    public static void dropDeleted() {
        deletedTasks = new HashMap<>();
        FileWorker.refreshDeletedTaskFile(deletedTasks);
        System.out.println("Drop successful!");
        Menu.show();
    }

    public static void showAll() {
        if (isTasksListNotEmpty(tasks)) {
            ConsoleWorker.showIdsAndTitles(tasks);
            showInfo(tasks);
            return;
        }
        Menu.show();
    }

    private static void showInfo(Map<Integer, Task> map) {
        int id = ConsoleWorker.waitValidIdOrZeroForShow(map);
        if (id == 0) Menu.show();
        else showInfoById(id, map);
    }

    private static void showInfoById(int id, Map<Integer, Task> map) {
        Task task = map.get(id);
        System.out.println(task);
        editOrReturn(id, map);
    }

    private static void editOrReturn(int id, Map<Integer, Task> map) {
        int choose = ConsoleWorker.waitChooseForEdit();
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
        map.get(id).setTitle(ConsoleWorker.waitNewTitle());
        FileWorker.refreshFiles(tasks, deletedTasks);
        showInfoById(id, map);
    }

    private static void editDescription(int id, Map<Integer, Task> map) {
        map.get(id).setDescription(ConsoleWorker.waitNewDescription());
        FileWorker.refreshFiles(tasks, deletedTasks);
        showInfoById(id, map);
    }
}