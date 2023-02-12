package Manager;

public class Managers<T extends TaskManager> {
    public TaskManager getDefault(){
        return new InMemoryTaskManager(getDefaultHistory());
    }
    public HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
