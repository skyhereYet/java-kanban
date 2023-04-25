package Tasks;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

public class EpicTask extends Task {
    private ArrayList<SubTask> storageSubtask = new ArrayList<>();

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
    public ArrayList<SubTask> getStorageSubtask() {
        return storageSubtask;
    }

    //сеттер списка подзадач SubTask
    public void setStorageSubtask(ArrayList <SubTask> storageSubtask) {
        this.storageSubtask = storageSubtask;
    }

    public void addSubtaskToEpicTask(SubTask subTask){
        storageSubtask.add(subTask);
    }

    public void eraseStorageSubTask() {
        storageSubtask.clear();
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

    public void removeSubTask(SubTask subtask) {
        storageSubtask.remove(subtask);
    }

    public void setTime() {
        if (storageSubtask.isEmpty()) {
            setStartTime(null);
            setDuration(null);
            setEndTime();
            return;
        }
        //отсортировать список subtask по startTime
        Collections.sort(storageSubtask, (task1, task2) -> {
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
        setStartTime(storageSubtask.get(0).getStartTime());
        //подсчитать duration по всем subTask и присвоить EpicTask
        Duration durationSubtasks = Duration.ofMinutes(0);
        for (SubTask subtask : storageSubtask) {
            if (subtask.getDuration() != null) {
                durationSubtasks = durationSubtasks.plus(subtask.getDuration());
            }
        }
        setDuration(durationSubtasks);
        setEndTime();
        /**Замечание:
         * метод расчета endTime, который используется для задач и подзадач не подходит для эпика.
         * Из ТЗ про это: "время завершения — время окончания самой поздней из задач".
         * Так же просто взять последнюю из отсортированных задач не подойдет. Потому что есть вероятность,
         * что они будут с null'ами
         *
         * Ответ:
         * В моей реализации setEndTime время завершения высчитывается на основании startTime и duration. Перебирать
         * сабтаски для поиска дейтсвительного endTime кажется нецелесообразным, если мы уже 2 параметра проверили ранее
         * и duration посчитана по всем подзадачам.
         * Т.е. у нас уже точно известно:
         *  - если начальное время существует и продолжительность эпика существует, и она выше рассчитана с учетом
         *      всех подзадач, тогда endTime = startTime + durationEpic;
         *  - если startTime = null или duration = null - значит endTime тоже null или равна startTime.
         *
         *  setTime() писался с целью унификации, если я ошибаюсь, тогда что я не учёл?)))
         */

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

}


