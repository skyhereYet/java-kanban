package Tasks;

import Manager.TasksType;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;
    private ZonedDateTime startTime;
    private Duration duration;
    protected ZonedDateTime endTime;
    protected TasksType tasksType = TasksType.TASK;


    public Task(String name, String description, int id, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, TaskStatus status, ZonedDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        setEndTime();
    }

    public Task(String name, String description, int id, TaskStatus status, ZonedDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        setEndTime();
    }

    public Task() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    public Duration getDuration() {
        if (duration == null) {
            return Duration.ofMinutes(0);
        }
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime() {
        if ((startTime == null) || (duration == null)) {
            endTime = startTime;
        } else {
            endTime = startTime.plus(duration);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) &&
                status == task.status && Objects.equals(startTime, task.startTime) &&
                Objects.equals(duration, task.duration) && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, startTime, duration, endTime);
    }

    public Boolean overlaps(Task other) {
        if (!this.startTime.isAfter(other.getStartTime())) {
            return overlaps(this, other);
        } else {
            return overlaps(other, this);
        }
    }

    private static boolean overlaps(Task task1, Task task2) {
        return (task1.getEndTime().isAfter(task2.getStartTime()));
    }
}






















