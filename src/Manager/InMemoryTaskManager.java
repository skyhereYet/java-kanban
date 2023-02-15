package Manager;

import Tasks.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    //Хранилище простых задач Task
    private HashMap<Integer, Task> storageTask = new HashMap<>();
    //Хранилище EpicTask
    private HashMap<Integer, EpicTask> storageEpicTask = new HashMap<>();
    //Хранилище SubTask
    private HashMap<Integer, SubTask> storageSubTask = new HashMap<>();
    private int id = 0;
    //создаем хранилище истории
    private HistoryManager storageHistory = Managers.getDefaultHistory();

    @Override
    public int getId() {
        return id;
    }

    //создаем задачу Task на основе входящего объекта
    @Override
    public void createTask(Task task) {
        task.setId(createId());
        storageTask.put(task.getId(), task);
    }

    //создаем задачу EpicTask на основе входящего объекта
    @Override
    public void createEpicTask(EpicTask task) {
        task.setId(createId());
        storageEpicTask.put(id, task);
        //вносим в хранилище все подзадачи в EpicTask - код был на случай получения эпика с подзадачами,
        // в данной реализации этот функционал исчезает( или с фронта все подзадачи будут поступать новыми subtaskами?
    }

    //создаем задачу SubTask на основе входящего объекта
    @Override
    public void createSubTask(SubTask task) {
        task.setId(createId());
        storageSubTask.put(task.getId(), task);
        //добавляем подзадачу в EpicTask
        if (storageEpicTask.containsKey(task.getIdEpicTask())) {
            EpicTask changeEpicTask = storageEpicTask.get(task.getIdEpicTask());
            changeEpicTask.addSubtaskToEpicTask(task);
            //уточняем статус EpicTask
            changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
            //обновляем EpicTask
            storageEpicTask.put(task.getIdEpicTask(), changeEpicTask);
        }
    }

    //метод обновления задачи Task на основе входящего объекта
    @Override
    public void updateTask(Task task) {
        if (storageTask.containsKey(task.getId())) {
            storageTask.put(task.getId(), task);
        }
    }

    //метод обновления задачи EpicTask на основе входящего объекта
    @Override
    public void updateEpicTask(EpicTask task) {
        if (storageEpicTask.containsKey(task.getId())) {
            //удаляем подзадачи, т.к. они могут быть удалены в новом EpicTask
            HashMap<Integer, SubTask> storageTemp = storageEpicTask.get(task.getId()).getStorageSubtask();
            if (storageTemp != null) {
                for (Integer key : storageTemp.keySet()) {
                    storageSubTask.remove(key);
                }
            }
            //обновляем EpicTask
            EpicTask updateEpicTask = new EpicTask(task.getName(), task.getDescription(), task.getId(),
                    task.getStatus());
            updateEpicTask.setStorageSubtask(task.getStorageSubtask());
            storageEpicTask.put(updateEpicTask.getId(), updateEpicTask);
            //обновляем хранилище SubTask
            storageTemp = task.getStorageSubtask();
            if (storageTemp != null) {
                for (Integer key : storageTemp.keySet()) {
                    storageSubTask.put(key, storageTemp.get(key));
                }
            }
        }
    }

    //метод обновления задачи SubTask в т.ч. в EpicTask
    @Override
    public void updateSubTask(SubTask task) {
        if (storageSubTask.containsKey(task.getId())) {
            int oldIdEpicTask = storageSubTask.get(task.getId()).getIdEpicTask();
            SubTask updateSubTask = new SubTask(task.getName(), task.getDescription(), task.getId(), task.getStatus(),
                    task.getIdEpicTask());
            storageSubTask.put(updateSubTask.getId(), updateSubTask);

            //проверяем принадлежность SubTask на изменение
            if (oldIdEpicTask != task.getIdEpicTask()) {
                //storage: удаляем из старого EpicTask подзадачу
                EpicTask changeEpicTask = storageEpicTask.get(oldIdEpicTask);
                changeEpicTask.removeSubTask(updateSubTask.getId());
                changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
                storageEpicTask.put(changeEpicTask.getId(), changeEpicTask);
                //добавляем подзадачу в новый EpicTask
                changeEpicTask = storageEpicTask.get(task.getIdEpicTask());
                changeEpicTask.addSubtaskToEpicTask(updateSubTask);
                changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
                storageEpicTask.put(changeEpicTask.getId(), changeEpicTask);
            } else {
                EpicTask changeEpicTask = storageEpicTask.get(task.getIdEpicTask());
                changeEpicTask.addSubtaskToEpicTask(updateSubTask);
                changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
                storageEpicTask.put(changeEpicTask.getId(), changeEpicTask);
            }
        }
    }

    //метод обновления статуса
    @Override
    public TaskStatus statusCheckerEpicTask(EpicTask changeEpicTask) {
        TaskStatus status = null;
        for (Integer Key : changeEpicTask.getStorageSubtask().keySet()) {
            SubTask tempSubTask = changeEpicTask.getStorageSubtask().get(Key);
            if (tempSubTask.getStatus() == TaskStatus.IN_PROGRESS) {
                status = TaskStatus.IN_PROGRESS;
            } else if ((tempSubTask.getStatus() == TaskStatus.DONE)
                    && (tempSubTask.getStatus() != TaskStatus.IN_PROGRESS)
                    && (tempSubTask.getStatus() != TaskStatus.NEW)) {
                status = TaskStatus.DONE;
            } else if ((tempSubTask.getStatus() == TaskStatus.NEW)
                    && (tempSubTask.getStatus() != TaskStatus.IN_PROGRESS)
                    && (tempSubTask.getStatus() != TaskStatus.DONE)) {
                status = TaskStatus.NEW;
            }
        }
        return status;
    }

    //метод создания уникального id
    @Override
    public int createId(){
        return ++id;
    }

    //метод возврата списка Task
    @Override
    public ArrayList<Task> getStorageTask() {
        ArrayList<Task> listTask = new ArrayList<>();
        for (Integer key : storageTask.keySet()) {
            listTask.add(storageTask.get(key));
        }
        return listTask;
    }

    //метод возврата списка EpicTask
    @Override
    public ArrayList<EpicTask> getStorageEpicTask() {
        ArrayList<EpicTask> listEpicTask = new ArrayList<>();
        for (Integer key : storageEpicTask.keySet()) {
            listEpicTask.add(storageEpicTask.get(key));
        }
        return listEpicTask;
    }

    //метод возврата списка SubTask
    @Override
    public ArrayList<SubTask> getStorageSubTask() {
        ArrayList<SubTask> listSubTask = new ArrayList<>();
        for (Integer key : storageSubTask.keySet()) {
            listSubTask.add(storageSubTask.get(key));
        }
        return listSubTask;
    }

    //очистка хранилища задач Task
    @Override
    public void eraseStorageTask(){
        storageTask.clear();
    }

    //очистка хранилища задач EpicTask
    @Override
    public void eraseStorageEpicTask(){
        storageEpicTask.clear();
        eraseStorageSubTask();
    }

    //очистка хранилища задач SubTask
    @Override
    public void eraseStorageSubTask(){
        storageSubTask.clear();
        for (Integer key : storageEpicTask.keySet()) {
            storageEpicTask.get(key).eraseStorageSubTask();
            storageEpicTask.get(key).setStatus(TaskStatus.NEW);
        }
    }

    //получение Task по id
    @Override
    public Task getTaskById (int idSearch) {
        storageHistory.add(storageTask.get(idSearch));
        return storageTask.get(idSearch);
    }

    //получение EpicTask по id
    @Override
    public EpicTask getEpicTaskById (int idSearch) {
        storageHistory.add(storageEpicTask.get(idSearch));
        return storageEpicTask.get(idSearch);
    }

    //получение SubTask по id
    @Override
    public Task getSubTaskById (int idSearch) {
        storageHistory.add(storageSubTask.get(idSearch));
        return storageSubTask.get(idSearch);
    }

    //удалить любую задачу по id
    @Override
    public void deleteAnyTaskById (int idSearch) {
        if (storageTask.containsKey(idSearch)) {
            storageTask.remove(idSearch);
        } else if (storageEpicTask.containsKey(idSearch)) {
            for (Integer key : storageEpicTask.get(idSearch).getStorageSubtask().keySet()) {
                storageSubTask.remove(key);
            }
            storageEpicTask.remove(idSearch);
        } else if (storageSubTask.containsKey(idSearch)) {
            //удаляем из EpicTask подзадачу
            EpicTask changeEpicTask = storageEpicTask.get(storageSubTask.get(idSearch).getIdEpicTask());
            changeEpicTask.removeSubTask(idSearch);
            //проверяем статус EpicTask
            changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
            storageEpicTask.put(changeEpicTask.getId(), changeEpicTask);
            storageSubTask.remove(idSearch);
        }
    }

    //вывести список подзадач EpicTask по id
    @Override
    public ArrayList<SubTask> getSubTaskByEpicId (int idSearch) {
        if (storageEpicTask.containsKey(idSearch)) {
            ArrayList<SubTask> listSubTask = new ArrayList<>();
            for (Integer key : storageEpicTask.get(idSearch).getStorageSubtask().keySet()) {
                listSubTask.add(storageSubTask.get(key));
            }
            return listSubTask;
        }
        return null;
    }

    //геттер хранилища истории
    @Override
    public ArrayList<Task> getHistory() {
        return storageHistory.getHistory();
    }
}
