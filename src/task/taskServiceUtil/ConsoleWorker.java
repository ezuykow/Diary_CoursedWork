package task.taskServiceUtil;

import menu.Menu;
import task.Task;
import task.TaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleWorker {

    public static int waitValidIdOrZeroForDelete(Map<Integer, Task> map) {
        int id = -1;
        Scanner in = new Scanner(System.in);
        while (!map.containsKey(id)) {
            System.out.println("Select the desired task:");
            showIdsAndTitles(map);
            System.out.println("Enter 0 to return to the menu");
            id = in.nextInt();
            if (id == 0) return 0;
        }
        return id;
    }

    public static int waitValidIdOrZeroForShow(Map<Integer, Task> map) {
        Scanner in = new Scanner(System.in);
        int id = -1;
        while (!map.containsKey(id) && id != 0) {
            System.out.println("Enter a 0 to return to menu or id to show info of task:");
            if (in.hasNextInt()){
                id = in.nextInt();
            }
        }
        return id;
    }

    public static int waitChooseForEdit() {
        System.out.println("-------------------------------------");
        Scanner in = new Scanner(System.in);
        int choose = -1;
        while (choose < 0 || choose > 2) {
            System.out.println("Enter a 0 to return, 1 to edit title or 2 to edit description:");
            if (in.hasNextInt()) {
                choose = in.nextInt();
            }
        }
        return choose;
    }

    public static String waitNewTitle() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a new title:");
        return in.nextLine();
    }

    public static String waitNewDescription() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a new description:");
        return in.nextLine();
    }

    public static String waitValidDateOrZero() {
        String day = "";
        Scanner in = new Scanner(System.in);
        while (!day.matches("\\d{2}\\.\\d{2}\\.\\d{4}") && !day.equals("0")) {
            System.out.println("Enter a date (format DD.MM.YYYY) or 0 to return:");
            day = in.nextLine();
        }
        return day;
    }

    public static void showIdsAndTitles(Map<Integer, Task> map) {
        map.forEach((k, v) -> System.out.println(
                "   " + k + ". " + v.getTitle()
        ));
    }

    public static void showIdsAndTitlesForDay(Map<Integer, Task> map, LocalDate ld) {
        Map<Integer, Task> temp = new HashMap<>();
        for (Map.Entry<Integer, Task> e : map.entrySet()) {
            if (e.getValue().getTime().toLocalDate().equals(ld)) {
                temp.put(e.getKey(), e.getValue());
            }
        }
        showIdsAndTitles(temp);
    }
}
