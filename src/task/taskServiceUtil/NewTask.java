package task.taskServiceUtil;

import task.Task;
import task.TaskType;
import task.byKind.*;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class NewTask {

    private final Task newTask;

    private NewTask() {
        int repeatType = waitRepeatType();
        newTask = createRepeatableTask(repeatType, waitTaskType(),
                waitTitle(), waitDescription(), waitDateTime());
    }

    public static Task getNewTask(){
        return new NewTask().newTask;
    }

    private Task createRepeatableTask(int type, TaskType taskType, String title, String description,
                                             LocalDateTime dateTime) {
        switch (type) {
            case 1:
                return new SingleTask(title, description, dateTime, taskType);
            case 2:
                return new DailyTask(title, description, dateTime, taskType);
            case 3:
                return new WeeklyTask(title, description, dateTime, taskType);
            case 4:
                return new MonthlyTask(title, description, dateTime, taskType);
            case 5:
                return new AnnualTask(title, description, dateTime, taskType);
            default:
                throw new RuntimeException("Something wrong...");
        }
    }

    private int waitRepeatType() {
        Scanner in = new Scanner(System.in);
        int choose = 0;
        do {
            System.out.println("Select a repeat period of task:\n" +
                    "   1. Single task\n" +
                    "   2. Daily task\n" +
                    "   3. Weekly task\n" +
                    "   4. Monthly task\n" +
                    "   5. Annual task");
            choose = in.nextInt();
        } while ((choose < 1) || (choose > 5));
        return choose;
    }

    private TaskType waitTaskType() {
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("Select a task type:" +
                    "\n   1. " + TaskType.PERSONAL.name().toLowerCase() +
                    "\n   2. " + TaskType.WORK.name().toLowerCase());
            switch (in.nextInt()) {
                case 1:
                    return TaskType.PERSONAL;
                case 2:
                    return TaskType.WORK;
            }
        } while (true);
    }

    private String waitTitle() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a title of task:");
        return in.nextLine();
    }

    private String waitDescription() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a description of task:");
        return in.nextLine();
    }

    private LocalDateTime waitDateTime() {
        Scanner in = new Scanner(System.in);
        LocalDateTime ldt = null;
        do {
            System.out.println("Enter a date and time of task (format: DD.MM.YYYY hh:mm):");
            String dateTime = in.next() + " " + in.next();
            if (dateTime.matches("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}")) {
                ldt = parseDateTimeIfValid(dateTime);
            }
        } while (ldt == null);
        return ldt;
    }

    private LocalDateTime parseDateTimeIfValid(String s) {
        String[] dateAndTime = s.split(" ");
        String[] dateParts = dateAndTime[0].split("\\.");
        String[] timeParts = dateAndTime[1].split(":");

        LocalDateTime ldt;
        try {
            ldt = LocalDateTime.of(Integer.parseInt(dateParts[2]),
                    Integer.parseInt(dateParts[1]),
                    Integer.parseInt(dateParts[0]),
                    Integer.parseInt(timeParts[0]),
                    Integer.parseInt(timeParts[1]));
        } catch (DateTimeException e) {
            return null;
        }
        if (ldt.isBefore(LocalDateTime.now())) return null;
        return ldt;
    }

}
