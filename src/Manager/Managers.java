package Manager;

import Tasks.Task;

import java.io.File;

public abstract class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
    public static TaskManager getFilBackedTasksManager(File filename) {
        return new FileBackedTasksManager(filename);
    }
}
