package Tasks;
public class SubTask extends Task{
   private int idEpicTask;

    public SubTask() {
        super();
    }

    public SubTask(String name, String description, int id, TaskStatus status, int idEpicTask) {
        super(name, description, id, status);
        this.idEpicTask = idEpicTask;
    }

    public SubTask(String name, String description, TaskStatus status, int idEpicTask) {
        super(name, description, status);
        this.idEpicTask = idEpicTask;
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
                "idEpicTask=" + idEpicTask +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}
