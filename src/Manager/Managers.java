package Manager;

import java.io.File;

public abstract class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager loadFromFile(File fileName) {
        return new FileBackedTasksManager(fileName);
    }
}
