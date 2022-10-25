package task.byKind;

import task.Task;
import task.TaskType;

import java.time.LocalDateTime;

public class DailyTask extends Task {

    public DailyTask(String title, String description, LocalDateTime time, TaskType type) {
        super(title, description, time, type, "daily");
    }

    @Override
    protected LocalDateTime nextRepeat() {
        return this.getTime().plusDays(1);
    }
}
