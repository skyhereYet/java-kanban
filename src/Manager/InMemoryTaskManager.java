package Manager;

import Tasks.*;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    //Хранилище простых задач Task
    protected HashMap<Integer, Task> storageTask = new HashMap<>();
    //Хранилище EpicTask
    protected HashMap<Integer, EpicTask> storageEpicTask = new HashMap<>();
    //Хранилище SubTask
    protected HashMap<Integer, SubTask> storageSubTask = new HashMap<>();
    protected int id = 1;
    //хранилище истории
    protected HistoryManager storageHistory = Managers.getDefaultHistory();

    //хранилище отсортированных по времени начала task
    protected TreeSet<Task> storageTaskByTime;

    public InMemoryTaskManager() {
        storageTaskByTime = new TreeSet(Comparator.nullsLast(dateTimeComparator));
    }


    @Override
    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    //создаем задачу Task на основе входящего объекта
    @Override
    public void createTask(Task task) {
        if (!validationTasks(task)) {
            throw new ManagerTimeException("Обнаружено пересечение задач по времени!");
        }
        task.setId(createId());
        storageTask.put(task.getId(), task);
        storageTaskByTime.add(task);
    }

    //создаем задачу EpicTask на основе входящего объекта
    @Override
    public void createEpicTask(EpicTask task) {
        if (!validationTasks(task)) {
            throw new ManagerTimeException("Обнаружено пересечение задач по времени!");
        }
        task.setId(createId());
        storageEpicTask.put(task.getId(), task);
    }

    //создаем задачу SubTask на основе входящего объекта
    @Override
    public void createSubTask(SubTask task) {
        if (!validationTasks(task)) {
            throw new ManagerTimeException("Обнаружено пересечение задач по времени!");
        }
        if (!storageEpicTask.containsKey(task.getIdEpicTask())) {
            throw new IllegalArgumentException("Отсутствует Epic для подзадачи");
        }
        task.setId(createId());
        storageSubTask.put(task.getId(), task);
        //добавляем подзадачу в EpicTask
        if (storageEpicTask.containsKey(task.getIdEpicTask())) {
            EpicTask changeEpicTask = storageEpicTask.get(task.getIdEpicTask());
            changeEpicTask.addSubtaskToEpicTask(task);
            //уточняем статус EpicTask
            changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
            //обновить startTime, endTime и duration у EpicTask
            changeEpicTask.setTime();
            //обновляем EpicTask
            storageEpicTask.put(task.getIdEpicTask(), changeEpicTask);
            //положить сабтаски в хранилище времени
            storageTaskByTime.add(task);
        }
    }

    //метод обновления задачи Task на основе входящего объекта
    @Override
    public void updateTask(Task task) {
        if (!validationTasks(task)) {
            throw new ManagerTimeException("Обнаружено пересечение задач по времени!");
        }
        if (storageTask.containsKey(task.getId())) {
            storageTask.put(task.getId(), task);
            storageTaskByTime.add(task);
        }
    }

    //метод обновления задачи EpicTask на основе входящего объекта
    @Override
    public void updateEpicTask(EpicTask task) {
        if (!validationTasks(task)) {
            throw new ManagerTimeException("Обнаружено пересечение задач по времени!");
        }
        if (storageEpicTask.containsKey(task.getId())) {
            //удаляем подзадачи, т.к. они могут быть удалены в новом EpicTask
            HashMap<Integer, SubTask> storageTemp = storageEpicTask.get(task.getId()).getStorageSubtask();
            if (storageTemp != null) {
                for (Integer key : storageTemp.keySet()) {
                    storageTaskByTime.remove(storageSubTask.get(key));
                    storageSubTask.remove(key);
                }
            }
            //обновляем EpicTask
            EpicTask updateEpicTask = task;
            updateEpicTask.setStorageSubtask(task.getStorageSubtask());
            updateEpicTask.setTime();
            storageEpicTask.put(updateEpicTask.getId(), updateEpicTask);
            //обновляем хранилище SubTask
            storageTemp = task.getStorageSubtask();
            if (storageTemp != null) {
                for (Integer key : storageTemp.keySet()) {
                    storageSubTask.put(key, storageTemp.get(key));
                    storageTaskByTime.add(storageTemp.get(key));
                }
            }
        }
    }

    //метод обновления задачи SubTask в т.ч. в EpicTask
    @Override
    public void updateSubTask(SubTask task) {
        if (!validationTasks(task)) {
            throw new ManagerTimeException("Обнаружено пересечение задач по времени!");
        }
        if (storageSubTask.containsKey(task.getId())) {
            int oldIdEpicTask = storageSubTask.get(task.getId()).getIdEpicTask();
            SubTask updateSubTask = new SubTask(task.getName(), task.getDescription(), task.getId(), task.getStatus(),
                    task.getIdEpicTask(), task.getStartTime(),task.getDuration());
            storageSubTask.put(updateSubTask.getId(), updateSubTask);
            storageTaskByTime.add(updateSubTask);

            //проверяем принадлежность SubTask на изменение
            if (oldIdEpicTask != task.getIdEpicTask()) {
                //storage: удаляем из старого EpicTask подзадачу
                EpicTask changeEpicTask = storageEpicTask.get(oldIdEpicTask);
                changeEpicTask.removeSubTask(updateSubTask.getId());
                changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
                changeEpicTask.setTime();
                storageEpicTask.put(changeEpicTask.getId(), changeEpicTask);
                //добавляем подзадачу в новый EpicTask
                changeEpicTask = storageEpicTask.get(task.getIdEpicTask());
                changeEpicTask.addSubtaskToEpicTask(updateSubTask);
                changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
                changeEpicTask.setTime();
                storageEpicTask.put(changeEpicTask.getId(), changeEpicTask);
            } else {
                EpicTask changeEpicTask = storageEpicTask.get(task.getIdEpicTask());
                changeEpicTask.addSubtaskToEpicTask(updateSubTask);
                changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
                changeEpicTask.setTime();
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
                    && (status == null)
                    //&& (status != TaskStatus.IN_PROGRESS)
                    //&& (status != TaskStatus.NEW)
                    ) {
                status = TaskStatus.DONE;
            } else if ((tempSubTask.getStatus() == TaskStatus.NEW)
                    && (status != TaskStatus.IN_PROGRESS)
                    ) {
                status = TaskStatus.NEW;
            }
        }
        return status;
    }

    //метод создания уникального id
    @Override
    public int createId(){
        return id++;
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
        //очистить историю просмотра
        for (Integer key : storageTask.keySet()) {
            storageHistory.remove(storageTask.get(key));
            storageTaskByTime.remove(storageTask.get(key));
        }
        //очистить хранилище
        storageTask.clear();
    }

    //очистка хранилища задач EpicTask
    @Override
    public void eraseStorageEpicTask(){
        //очистить историю просмотра
        for (Integer key : storageEpicTask.keySet()) {
            storageHistory.remove(storageEpicTask.get(key));
            //storageTaskByTime.remove(storageEpicTask.get(key)); Эпиков в хранилище нет
        }
        //очистить хранилище
        storageEpicTask.clear();
        eraseStorageSubTask();
    }

    //очистка хранилища задач SubTask
    @Override
    public void eraseStorageSubTask(){
        //очистить историю просмотра
        for (Integer key : storageSubTask.keySet()) {
            storageHistory.remove(storageSubTask.get(key));
            SubTask t= storageSubTask.get(key);
            storageTaskByTime.remove(t);
        }
        //очистить хранилище
        storageSubTask.clear();
        //обновить статусы Epic и удалить список подзадач Epic
        for (Integer key : storageEpicTask.keySet()) {
            storageEpicTask.get(key).eraseStorageSubTask();
            storageEpicTask.get(key).setStatus(TaskStatus.NEW);
            storageEpicTask.get(key).setTime();
        }
    }

    //получение Task по id
    @Override
    public Task getTaskById (int idSearch) {
        if (storageTask.containsKey(idSearch)) {
            storageHistory.add(storageTask.get(idSearch));
            return storageTask.get(idSearch);
        }
        return null;
    }

    //получение EpicTask по id
    @Override
    public EpicTask getEpicTaskById (int idSearch) {
        if (storageEpicTask.containsKey(idSearch)) {
            storageHistory.add(storageEpicTask.get(idSearch));
            return storageEpicTask.get(idSearch);
        }
        return null;
    }

    //получение SubTask по id
    @Override
    public SubTask getSubTaskById (int idSearch) {
        if (storageSubTask.containsKey(idSearch)) {
            storageHistory.add(storageSubTask.get(idSearch));
            return storageSubTask.get(idSearch);
        }
        return null;
    }

    //удалить любую задачу по id
    @Override
    public void deleteAnyTaskById (int idSearch) {
        if (storageTask.containsKey(idSearch)) {
            //удалить из истории просмотра
            storageHistory.remove(storageTask.get(idSearch));
            //удалить из хранилища
            storageTaskByTime.remove(storageTask.get(idSearch));
            storageTask.remove(idSearch);
        } else if (storageEpicTask.containsKey(idSearch)) {
            for (Integer key : storageEpicTask.get(idSearch).getStorageSubtask().keySet()) {
                //удалить из истории просмотра
                storageHistory.remove(storageSubTask.get(key));
                //удалить из хранилища
                storageTaskByTime.remove(storageSubTask.get(key));
                storageSubTask.remove(key);
            }
            //удалить из истории просмотра
            storageHistory.remove(storageEpicTask.get(idSearch));
            //удалить из хранилища
            storageEpicTask.remove(idSearch);
        } else if (storageSubTask.containsKey(idSearch)) {
            //удаляем из EpicTask подзадачу
            EpicTask changeEpicTask = storageEpicTask.get(storageSubTask.get(idSearch).getIdEpicTask());
            changeEpicTask.removeSubTask(idSearch);
            //проверяем статус и время EpicTask
            changeEpicTask.setStatus(statusCheckerEpicTask(changeEpicTask));
            changeEpicTask.setTime();
            storageEpicTask.put(changeEpicTask.getId(), changeEpicTask);
            //удалить из истории просмотра
            storageHistory.remove(storageSubTask.get(idSearch));
            //удалить из хранилища
            storageTaskByTime.remove(storageSubTask.get(idSearch));
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
    public List<Task> getHistory() {
        return storageHistory.getHistory();
    }

    //компаратор по startTime
    Comparator<Task> dateTimeComparator = (task1, task2) -> {
        if (task1.getStartTime() == task2.getStartTime()) {
            return 0;
        } else if (task2.getStartTime() == null) {
            return -1;
        } else if (task1.getStartTime() == null) {
            return -1;
        }
        return task1.getStartTime().toLocalTime().compareTo(task2.getStartTime().toLocalTime());
    };

    //геттер сортированного по времени списка всех task
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(storageTaskByTime);
    }

    //валидатор пересечении времени
    public Boolean validationTasks(Task newTask) {
        if (newTask.getStartTime() != null) {
            for (Task task : storageTaskByTime) {
                if (task.overlaps(newTask) && task.getId() != newTask.getId()) {
                    return false;
                }
            }
        }
        return true;
    }
}
