package menu;

import task.TaskService;

public enum Items {
    ADD_TASK(1, "Add new task"),
    ALL_TASKS(2, "Show all tasks"),
    TASKS_BY_DAYS(3, "Get grouped by days tasks"),
    TASK_OF_DAY(4, "Get a task for a specified day"),
    DELETE_TASK(5, "Delete task"),
    SHOW_DELETED(6, "Show deleted tasks"),
    DROP_DELETED(7, "Drop deleted tasks"),
    EXIT(0, "Exit");

    private final int num;
    private final String text;

    Items(int n, String t) {
        num = n;
        text = t;
    }

    public int getNum() {
        return num;
    }

    public String getText() {
        return text;
    }

    public static void doAction(Items item) {
        if (item == null) {
            Menu.show();
        } else {
            switch (item) {
                case ADD_TASK:
                    TaskService.createNewTask();
                    break;
                case DELETE_TASK:
                    TaskService.deleteTask();
                    break;
                case ALL_TASKS:
                    TaskService.showAll();
                    break;
                case TASKS_BY_DAYS:
                    TaskService.getGroupedByDaysTasks();
                    break;
                case TASK_OF_DAY:
                    TaskService.getTasksByDay();
                    break;
                case SHOW_DELETED:
                    TaskService.showDeletedTasks();
                    break;
                case DROP_DELETED:
                    TaskService.dropDeleted();
                case EXIT:
                    System.exit(0);
                    break;
            }
        }
    }
}
