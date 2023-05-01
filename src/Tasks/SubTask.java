package Tasks;

import Manager.TasksType;

import java.time.Duration;
import java.time.ZonedDateTime;

public class SubTask extends Task implements Comparable<SubTask> {
   private int idEpicTask;


    public SubTask(String name, String description, int id, TaskStatus status, int idEpicTask,
                   ZonedDateTime startTime, Duration duration) {
        super(name, description, id, status, startTime, duration);
        this.idEpicTask = idEpicTask;
        this.tasksType = TasksType.SUBTASK;
    }

    public SubTask(String name, String description, int id, TaskStatus status, int idEpicTask) {
        super(name, description, id, status);
        this.idEpicTask = idEpicTask;
        this.tasksType = TasksType.SUBTASK;
    }

    public SubTask(String name, String description, TaskStatus status, int idEpicTask) {
        super(name, description, status);
        this.idEpicTask = idEpicTask;
        this.tasksType = TasksType.SUBTASK;
    }

    public SubTask() {
        this.tasksType = TasksType.SUBTASK;
    }

    public int getIdEpicTask() {
        return idEpicTask;
    }

    public void setIdEpicTask(int idEpicTask) {
        this.idEpicTask = idEpicTask;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", idEpicTask=" + idEpicTask +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", duration='" + getDuration() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}';
    }

   @Override
    public int compareTo(SubTask otherTask) {
        if (this.getStartTime() == otherTask.getStartTime()) {
            return 1;
        } else if (otherTask.getStartTime() == null) {
            return -1;
        } else if (this.getStartTime() == null) {
            return -1;
        }
        return this.getStartTime().toLocalTime().compareTo(otherTask.getStartTime().toLocalTime());
    }
}
