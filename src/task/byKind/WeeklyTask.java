package task.byKind;

import task.Task;
import task.TaskType;

import java.time.LocalDateTime;

public class WeeklyTask extends Task {

    public WeeklyTask(String title, String description, LocalDateTime time, TaskType type) {
        super(title, description, time, type, RepeatType.WEEKLY);
    }

    @Override
    protected LocalDateTime nextRepeat() {
        return this.getTime().plusWeeks(1);
    }
}
