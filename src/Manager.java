import java.util.HashMap;

public class Manager {
    //Хранилище простых задач Task
    private HashMap<Integer, Task> storageTask = new HashMap<>();
    //Хранилище EpicTask
    private HashMap<Integer, EpicTask> storageEpicTask = new HashMap<>();
    //Хранилище SubTask
    private HashMap<Integer, SubTask> storageSubTask = new HashMap<>();
    private int id = 0;

    public int getId() {
        return id;
    }

    //создаем задачу Task на основе входящего объекта
    void createTask(Task task) {
        Task newTask = new Task();
        newTask.setId(createId());
        newTask.setName(task.getName());
        newTask.setStatus(TaskStatus.NEW);
        newTask.setDescription(task.getDescription());
        storageTask.put(newTask.getId(), newTask);
    }

    //создаем задачу EpicTask на основе входящего объекта
    void createEpicTask(EpicTask task) {
        EpicTask newEpicTask = new EpicTask();
        newEpicTask.setId(createId());
        newEpicTask.setName(task.getName());
        newEpicTask.setStatus(TaskStatus.NEW);
        newEpicTask.setDescription(task.getDescription());
        storageEpicTask.put(id, newEpicTask);
        //вносим в хранилище все подзадачи в EpicTask
        HashMap<Integer, SubTask> storageTemp = new HashMap<>(task.getStorageSubtask());
        if (storageTemp != null) {
            for (Integer key : storageTemp.keySet()) {
                createSubTask(storageTemp.get(key));
                newEpicTask.addSubtaskToEpicTask(storageSubTask.get(id));
            }
        }
    }

    //создаем задачу SubTask на основе входящего объекта
    void createSubTask(SubTask task) {
        SubTask newSubTask = new SubTask();
        newSubTask.setId(createId());
        newSubTask.setName(task.getName());
        newSubTask.setStatus(TaskStatus.NEW);
        newSubTask.setDescription(task.getDescription());
        newSubTask.setIdEpicTask(task.getIdEpicTask());
        storageSubTask.put(newSubTask.getId(), newSubTask);
        //добавляем подзадачу в EpicTask
        if (storageEpicTask.containsKey(task.getIdEpicTask())) {
            EpicTask changeEpicTask = storageEpicTask.get(task.getIdEpicTask());
            changeEpicTask.addSubtaskToEpicTask(newSubTask);
            //уточняем статус EpicTask
            changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
            //обновляем EpicTask
            storageEpicTask.put(task.getIdEpicTask(), changeEpicTask);
        }
    }

    //метод обновления задачи Task на основе входящего объекта
    void updateTask(Task task) {
        Task updateTask = storageTask.get(task.getId());
        updateTask.setName(task.getName());
        updateTask.setStatus(task.getStatus());
        updateTask.setDescription(task.getDescription());
        storageTask.put(updateTask.getId(), updateTask);
    }

    //метод обновления задачи EpicTask на основе входящего объекта
    void updateEpicTask(EpicTask task) {
        EpicTask updateEpicTask = storageEpicTask.get(task.getId());
        //удаляем подзадачи, т.к. они могут быть удалены в новом EpicTask
        HashMap<Integer, SubTask> storageTemp = updateEpicTask.getStorageSubtask();
        if (storageTemp != null) {
            for (Integer key : storageTemp.keySet()) {
                storageSubTask.remove(key);
            }
        }
        //обновляем EpicTask
        updateEpicTask.setName(task.getName());
        updateEpicTask.setStatus(task.getStatus());
        updateEpicTask.setDescription(task.getDescription());
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

    //метод обновления задачи SubTask в т.ч. в EpicTask
    void updateSubTask(SubTask task) {
        SubTask updateSubTask = storageSubTask.get(task.getId());
        int oldIdEpicTask = updateSubTask.getIdEpicTask();
        updateSubTask.setName(task.getName());
        updateSubTask.setStatus(task.getStatus());
        updateSubTask.setDescription(task.getDescription());
        updateSubTask.setIdEpicTask(task.getIdEpicTask());
        storageSubTask.put(updateSubTask.getId(), updateSubTask);
        //проверяем принадлежность SubTask на изменение
        if (oldIdEpicTask != task.getIdEpicTask()) {
            //удаляем из старого EpicTask подзадачу
            EpicTask changeEpicTask = storageEpicTask.get(oldIdEpicTask);
            changeEpicTask.removeSubTask(updateSubTask.getId());
            changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
            storageEpicTask.put(changeEpicTask.getId(), changeEpicTask);
            //добавляем подзадачу в новый EpicTask
            changeEpicTask = storageEpicTask.get(task.getIdEpicTask());
            changeEpicTask.addSubtaskToEpicTask(updateSubTask);
            changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
            storageEpicTask.put(changeEpicTask.getId(), changeEpicTask);
        }
    }

    //метод обновления статуса
    TaskStatus statusCheckerEpicTask(EpicTask changeEpicTask) {
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
    int createId(){
        return ++id;
    }

    //метод возврата списка Task
    public HashMap<Integer, Task> getStorageTask() {
        return storageTask;
    }

    //метод возврата списка EpicTask
    public HashMap<Integer, EpicTask> getStorageEpicTask() {
        return storageEpicTask;
    }

    //метод возврата списка SubTask
    public HashMap<Integer, SubTask> getStorageSubTask() {
        return storageSubTask;
    }

    //очистка хранилища задач Task
    void eraseStorageTask(){
        storageTask.clear();
    }

    //очистка хранилища задач EpicTask
    void eraseStorageEpicTask(){
        storageEpicTask.clear();
    }

    //очистка хранилища задач SubTask
    void eraseStorageSubTask(){
        storageSubTask.clear();
    }

    //получение Task по id
    Task getTaskById (int idSearch) {
        return storageTask.get(idSearch);
    }

    //получение EpicTask по id
    EpicTask getEpicTaskById (int idSearch) {
        return storageEpicTask.get(idSearch);
    }

    //получение SubTask по id
    Task getSubTaskById (int idSearch) {
        return storageSubTask.get(idSearch);
    }

    //удалить любую задачу по id
    void deleteAnyTaskById (int idSearch) {
        if (storageTask.containsKey(idSearch)) {
            storageTask.remove(idSearch);
        } else if (storageEpicTask.containsKey(idSearch)) {
            storageEpicTask.remove(idSearch);
        } else if (storageSubTask.containsKey(idSearch)) {
            storageSubTask.remove(idSearch);
        }
    }

    //вывести список подзадач EpicTask по id
    HashMap<Integer, SubTask> getSubTaskByEpicId (int idSearch) {
        if (storageEpicTask.containsKey(idSearch)) {
            return storageEpicTask.get(idSearch).getStorageSubtask();
        }
        return null;
    }
}
