package Tasks;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeSet;

public class EpicTask extends Task {
    private HashMap<Integer, SubTask> storageSubtask = new HashMap<>();
    private TreeSet<SubTask> storageSubtaskByTime = new TreeSet<>(Comparator.nullsLast((task1, task2) -> {
        if (task1.getStartTime() == task2.getStartTime()) {
            return 1;
        } else if (task2.getStartTime() == null) {
            return -1;
        } else if (task1.getStartTime() == null) {
            return -1;
        }
        return task1.getStartTime().toLocalTime().compareTo(task2.getStartTime().toLocalTime());}));

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

    /*public EpicTask(String name, String description, TaskStatus status, ZonedDateTime startTime,
                    Duration duration) {
        super(name, description, status, startTime, duration);
    }*/

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


    public void setTime() {
        if (!storageSubtask.isEmpty()) {
            //отсортировать список subtask по startTime
            storageSubtaskByTime.clear();
            storageSubtask.forEach((key, task) -> storageSubtaskByTime.add(task));
            //установить в Epic startTime по первому subTask
            setStartTime(storageSubtaskByTime.first().getStartTime());
            //подсчитать duration по всем subTask b присвоить EpicTask
            Duration durationSubtasks = Duration.ofMinutes(0);
            //storageSubtaskByTime.forEach(v-> durationSubtasks.plus(v.getDuration()));
            for (SubTask subtask : storageSubtaskByTime) {
                durationSubtasks = durationSubtasks.plus(subtask.getDuration());
            }
            setDuration(durationSubtasks);
            //установить endTime Epictask
            setEndTime();
        } else {
            setStartTime(null);
            setDuration(null);
        }
    }
}
