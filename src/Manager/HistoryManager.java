package Manager;

import Tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {
    //вывести историю просмотров задач
    ArrayList<Task> getHistory();

    //заполнить историю просмотров задач
    void add(Task task);
}
