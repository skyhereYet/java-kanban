package Tasks;
import java.util.HashMap;

public class EpicTask extends Task {
    private HashMap<Integer, SubTask> storageSubtask = new HashMap<>();

    //конструктор
    public EpicTask() {
        super();
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
    }

    public void addSubtaskToEpicTask(SubTask subTask){
        storageSubtask.put(subTask.getId(), subTask);
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
                '}';
    }

    public void removeSubTask(int id) {
        storageSubtask.remove(id);
    }
}
