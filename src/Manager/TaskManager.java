package Manager;

import Tasks.*;
import java.util.ArrayList;

public interface TaskManager {

    int getId();

    //создаем задачу Task на основе входящего объекта
    void createTask(Task task);

    //создаем задачу EpicTask на основе входящего объекта
    void createEpicTask(EpicTask task);

    //создаем задачу SubTask на основе входящего объекта
    void createSubTask(SubTask task);

    //метод обновления задачи Task на основе входящего объекта
    void updateTask(Task task);

    //метод обновления задачи EpicTask на основе входящего объекта
    void updateEpicTask(EpicTask task);

    //метод обновления задачи SubTask в т.ч. в EpicTask
   void updateSubTask(SubTask task);

    //метод обновления статуса
    TaskStatus statusCheckerEpicTask(EpicTask changeEpicTask);

    //метод создания уникального id
    int createId();

    //метод возврата списка Task
    ArrayList<Task> getStorageTask();

    //метод возврата списка EpicTask
    ArrayList<EpicTask> getStorageEpicTask();

    //метод возврата списка SubTask
    ArrayList<SubTask> getStorageSubTask();

    //очистка хранилища задач Task
    void eraseStorageTask();

    //очистка хранилища задач EpicTask
    void eraseStorageEpicTask();

    //очистка хранилища задач SubTask
    void eraseStorageSubTask();

    //получение Task по id
    Task getTaskById (int idSearch);

    //получение EpicTask по id
    EpicTask getEpicTaskById (int idSearch);

    //получение SubTask по id
    Task getSubTaskById (int idSearch);

    //удалить любую задачу по id
    void deleteAnyTaskById (int idSearch);

    //вывести список подзадач EpicTask по id
    ArrayList<SubTask> getSubTaskByEpicId (int idSearch);

    //геттер хранилища истории
    ArrayList<Task> getHistory();//вот это я балбесина)))))
}
