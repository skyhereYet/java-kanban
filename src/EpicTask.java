import java.util.HashMap;

public class EpicTask extends Task {
    private HashMap<Integer, SubTask> storageSubtask = new HashMap<>();

    //конструктор
    public EpicTask() {
        super();
    }

    protected EpicTask(String name, String description, int id, TaskStatus status, HashMap<Integer,
            SubTask> storageSubtask) {
        super(name, description, id, status);
        this.storageSubtask = storageSubtask;
    }

    //геттер списка подзадач SubTask
    public HashMap<Integer, SubTask> getStorageSubtask() {
        return storageSubtask;
    }

    //сеттер списка подзадач SubTask
    public void setStorageSubtask(HashMap<Integer, SubTask> storageSubtask) {
        this.storageSubtask = storageSubtask;
    }

    void addSubtaskToEpicTask(SubTask subTask){
        storageSubtask.put(subTask.getId(), subTask);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", storageSubtask=" + storageSubtask +
                '}';
    }

    public void removeSubTask(int id) {
        storageSubtask.remove(id);
    }
}
