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
    public void updateEpicTask(EpicTask updateEpicTask) {
        if (!validationTasks(updateEpicTask)) {
            throw new ManagerTimeException("Обнаружено пересечение задач по времени!");
        }
        if (storageEpicTask.containsKey(updateEpicTask.getId())) {
            //удаляем подзадачи, т.к. они могут быть удалены в новом EpicTask
            ArrayList <SubTask> storageTemp = storageEpicTask.get(updateEpicTask.getId()).getStorageSubtask();
            if (storageTemp != null) {
                for (SubTask subTask : storageTemp) {
                    storageTaskByTime.remove(subTask);
                    storageSubTask.remove(subTask);
                }
            }
            //обновляем EpicTask
            updateEpicTask.setStorageSubtask(updateEpicTask.getStorageSubtask());
            updateEpicTask.setTime();
            storageEpicTask.put(updateEpicTask.getId(), updateEpicTask);
            //обновляем хранилище SubTask
            storageTemp = updateEpicTask.getStorageSubtask();
            if (storageTemp != null) {
                for (SubTask subTask : storageTemp) {
                    storageSubTask.put(subTask.getId(), subTask);
                    storageTaskByTime.add(subTask);
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
            SubTask oldSubTask = storageSubTask.get(task.getId());
            SubTask updateSubTask = new SubTask(task.getName(), task.getDescription(), task.getId(), task.getStatus(),
                    task.getIdEpicTask(), task.getStartTime(),task.getDuration());

            storageSubTask.put(updateSubTask.getId(), updateSubTask);
            storageTaskByTime.add(updateSubTask);

            //проверяем принадлежность SubTask на изменение
            if (oldSubTask.getIdEpicTask() != task.getIdEpicTask()) {
                //storage: удаляем из старого EpicTask подзадачу
                EpicTask changeEpicTask = storageEpicTask.get(oldSubTask.getIdEpicTask());
                changeEpicTask.removeSubTask(storageSubTask.get(updateSubTask.getId()));
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
                changeEpicTask.removeSubTask(oldSubTask);
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
        for (SubTask tempSubTask : changeEpicTask.getStorageSubtask()) {
            if (tempSubTask.getStatus() == TaskStatus.IN_PROGRESS) {
                status = TaskStatus.IN_PROGRESS;
            } else if ((tempSubTask.getStatus() == TaskStatus.DONE)
                    && (status == null)
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
            for (SubTask subtask : storageEpicTask.get(idSearch).getStorageSubtask()) {
                //удалить из истории просмотра
                storageHistory.remove(subtask);
                //удалить из хранилища
                storageTaskByTime.remove(subtask);
                storageSubTask.remove(subtask.getId());
            }
            //удалить из истории просмотра
            storageHistory.remove(storageEpicTask.get(idSearch));
            //удалить из хранилища
            storageEpicTask.remove(idSearch);
        } else if (storageSubTask.containsKey(idSearch)) {
            //удаляем из EpicTask подзадачу
            EpicTask changeEpicTask = storageEpicTask.get(storageSubTask.get(idSearch).getIdEpicTask());
            changeEpicTask.removeSubTask(storageSubTask.get(idSearch));
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
            return storageEpicTask.get(idSearch).getStorageSubtask();
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
        /**
         * По замечанию:
         * если внимательно причитаться к условиям задания, то цитирую: "Напишите новый метод getPrioritizedTasks,
         * возвращающий список задач и подзадач в заданном порядке". Задумано так, потому что Эпики сами по себе
         * каких-то отдельных действий не предполагают.
         *
         * Ответ:
         * Эпики в сортированный список не добавляются, в хранилище заносятся только Task и Subtask в момент их создания
         * или обновления
         */
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
