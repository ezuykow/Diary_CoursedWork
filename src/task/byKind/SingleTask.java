package task.byKind;

import task.Task;
import task.TaskType;

import java.time.LocalDateTime;

public class SingleTask extends Task {

    public SingleTask(String title, String description, LocalDateTime time, TaskType type) {
        super(title, description, time, type, RepeatType.SINGLE);
    }

    @Override
    protected LocalDateTime nextRepeat() {
        return null;
    }
}
