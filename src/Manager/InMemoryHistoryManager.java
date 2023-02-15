package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    //Хранилище истории просмотров
    private final List<Task> storageHistory = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return storageHistory;
    }

    @Override
    public void add(Task task) {
        if (storageHistory.size() >= 10) {
            storageHistory.remove(0);
            storageHistory.add(task);
        } else {
            storageHistory.add(task);
        }
    }
}
