package Tasks;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

public class EpicTask extends Task {
    private HashMap<Integer, SubTask> storageSubtask = new HashMap<>();
    private ZonedDateTime endTime;

    private ArrayList<SubTask> storageSubtaskByTime = new ArrayList<>();

    //конструктор
    public EpicTask(String name, String description, TaskStatus status, ZonedDateTime zonedDateTime, Duration duration) {
        super(name, description, status, zonedDateTime, duration);

    }
    public EpicTask(String name, String description, int id, TaskStatus status, ZonedDateTime startTime,
                    Duration duration) {
        super(name, description, id, status, startTime, duration);

    }

    public EpicTask(String name, String description, int id, TaskStatus status) {
        super(name, description, id, status);
    }

    public EpicTask(String name, String description, TaskStatus status) {
        super(name, description,status);
    }

    //геттер списка подзадач SubTask
    public HashMap<Integer, SubTask> getStorageSubtask() {
        return storageSubtask;
    }

    //сеттер списка подзадач SubTask
    public void setStorageSubtask(HashMap<Integer, SubTask> storageSubtask) {
        this.storageSubtask = storageSubtask;
        storageSubtask.forEach((key, task) -> storageSubtaskByTime.add(task));
    }

    public void addSubtaskToEpicTask(SubTask subTask){
        storageSubtask.put(subTask.getId(), subTask);
        storageSubtaskByTime.add(subTask);
    }

    public void eraseStorageSubTask() {
        storageSubtask.clear();
        storageSubtaskByTime.clear();
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", storageSubtask=" + storageSubtask +
                ", startTime='" + getStartTime() + '\'' +
                ", duration='" + getDuration() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getId(), getStatus(), getStartTime(), getDuration(), getEndTime());
    }

    public void removeSubTask(int id) {
        storageSubtaskByTime.remove(storageSubtask.get(id));
        storageSubtask.remove(id);
    }

    public ArrayList<SubTask> getStorageSubtaskByTime() {
        return storageSubtaskByTime;
    }

    public void setTime() {
        if (!storageSubtask.isEmpty()) {
            //отсортировать список subtask по startTime
            storageSubtaskByTime.clear();
            storageSubtask.forEach((key, task) -> storageSubtaskByTime.add(task));
            Collections.sort(storageSubtaskByTime, (task1, task2) -> {
                        if (task1.getStartTime() == task2.getStartTime()) {
                            return 0;
                        } else if (task2.getStartTime() == null) {
                            return -1;
                        } else if (task1.getStartTime() == null) {
                            return -1;
                        }
                        return task1.getStartTime().toLocalTime().compareTo(task2.getStartTime().toLocalTime());
                    }
            );
            //установить в Epic startTime по первому subTask
            setStartTime(storageSubtaskByTime.get(0).getStartTime());
            //подсчитать duration по всем subTask b присвоить EpicTask
            Duration durationSubtasks = Duration.ofMinutes(0);
            //storageSubtaskByTime.forEach(v-> durationSubtasks.plus(v.getDuration()));
            for (SubTask subtask : storageSubtaskByTime) {
                durationSubtasks = durationSubtasks.plus(subtask.getDuration());
            }
            setDuration(durationSubtasks);
            //установить endTime Epictask
            endTime = storageSubtaskByTime.get(storageSubtaskByTime.size()-1).getEndTime();
        } else {
            setStartTime(null);
            setDuration(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return getId() == task.getId() && Objects.equals(getName(), task.getName()) &&
                Objects.equals(getDescription(), task.getDescription()) &&
                getStatus() == task.getStatus() && Objects.equals(getStartTime(), task.getStartTime()) &&
                Objects.equals(getDuration(), task.getDuration()) && Objects.equals(getEndTime(), task.getEndTime());
    }
    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(storageSubtask, epicTask.storageSubtask) && Objects.equals(endTime, epicTask.endTime) && Objects.equals(storageSubtaskByTime, epicTask.storageSubtaskByTime);
    }*/
}


