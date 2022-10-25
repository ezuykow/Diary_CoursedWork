package task.byKind;

import task.Task;
import task.TaskType;

import java.time.LocalDateTime;

public class AnnualTask extends Task {

    public AnnualTask(String title, String description, LocalDateTime time, TaskType type) {
        super(title, description, time, type, "annual");
    }

    @Override
    protected LocalDateTime nextRepeat() {
        return this.getTime().plusYears(1);
    }
}
