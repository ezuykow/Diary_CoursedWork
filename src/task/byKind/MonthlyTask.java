package task.byKind;

import task.Task;
import task.TaskType;

import java.time.LocalDateTime;

public class MonthlyTask extends Task {

    public MonthlyTask(String title, String description, LocalDateTime time, TaskType type) {
        super(title, description, time, type, "monthly");
    }

    @Override
    protected LocalDateTime nextRepeat() {
        return this.getTime().plusMonths(1);
    }
}
