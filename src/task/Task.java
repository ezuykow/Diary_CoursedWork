package task;

import task.byKind.RepeatType;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Task implements Serializable {
    private String title;
    private String description;
    private final LocalDateTime time;
    private final TaskType type;
    private boolean isDeleted;
    private final RepeatType repeatType;

    public Task(String title, String description, LocalDateTime time, TaskType type, RepeatType repeatType) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.type = type;
        isDeleted = false;
        this.repeatType = repeatType;
    }

    protected abstract LocalDateTime nextRepeat();

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted() {
        if (!isDeleted) isDeleted = true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public TaskType getType() {
        return type;
    }

    public RepeatType getRepeatType() {
        return repeatType;
    }

    public String toString() {
        return "Title:\n   " + getTitle() +
                        "\nDescription:\n   " + getDescription() +
                        "\nTime:    " + getTime() +
                        "\nType:    " + getType() +
                        "\nRepeatability type:    " + repeatType +
                        "\nNext repeat:     " + (nextRepeat()==null ? "newer" : nextRepeat());
    }
}
