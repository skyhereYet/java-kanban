package Manager;

import Tasks.Task;

import java.util.List;

public interface HistoryManager {
    //вывести историю просмотров задач
    List<Task> getHistory();

    //заполнить историю просмотров задач
    void add(Task task);

    //удалить задачу из истории просмотров
    void remove(Task task);


}
