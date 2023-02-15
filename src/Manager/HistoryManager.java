package Manager;

import Tasks.Task;

import java.util.List;

public interface HistoryManager<T extends List<Task>> {
    //вывести историю просмотров задач
    List<Task> getHistory();

    //заполнить историю просмотров задач
    void add(Task task);
}
