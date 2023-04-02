package Manager;
import Tasks.*;
import java.io.*;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager  {
    private final File fileName;

    public FileBackedTasksManager(File fileName) {
        this.fileName=fileName;
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager1 = Managers.loadFromFile(new File("Resources\\",
                                                                                    "kanbanDB.csv"));
        makeTest(fileBackedTasksManager1);
        try {
            fileBackedTasksManager1.save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }

        FileBackedTasksManager fileBackedTasksManager2 = Managers.loadFromFile(new File("Resources\\",
                                                                                        "kanbanDB.csv"));
        try {
            fileBackedTasksManager2.read();
            //вывести Task с первого менеджера
            System.out.println("Task первого менеджера: ");
            for (Task task : fileBackedTasksManager1.getStorageTask()) {
                System.out.println(task);
            }
            System.out.println();
            //вывести Task со второго менеджера
            System.out.println("Task второго менеджера: ");
            for (Task task : fileBackedTasksManager2.getStorageTask()) {
                System.out.println(task);
            }
            System.out.println();
            //вывести SubTask с первого менеджера
            System.out.println("SubTask первого менеджера: ");
            for (Task task : fileBackedTasksManager1.getStorageSubTask()) {
                System.out.println(task);
            }
            System.out.println();
            //вывести SubTask со второго менеджера
            System.out.println("SubTask второго менеджера: ");
            for (Task task : fileBackedTasksManager2.getStorageSubTask()) {
                System.out.println(task);
            }
            System.out.println();
            //вывести EpicTask с первого менеджера
            System.out.println("EpicTask первого менеджера: ");
            for (Task task : fileBackedTasksManager1.getStorageSubTask()) {
                System.out.println(task);
            }
            System.out.println();
            //вывести EpicTask со второго менеджера
            System.out.println("EpicTask второго менеджера: ");
            for (Task task : fileBackedTasksManager2.getStorageSubTask()) {
                System.out.println(task);
            }
            System.out.println();
            //вывести историю
            System.out.println("История просмотра первого менеджера: ");
            for (Task task: fileBackedTasksManager1.getHistory()) {
                System.out.print(task.getId() + ",");
            }
            System.out.println("\n");
            System.out.println("История просмотра второго менеджера: ");
            for (Task task: fileBackedTasksManager2.getHistory()) {
                System.out.print(task.getId() + ",");
            }
            System.out.println("\n\n"+ "id первого менеджера: " + fileBackedTasksManager1.getId());
            System.out.println("\n" + "id второго менеджера: " + fileBackedTasksManager2.getId());

        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void makeTest(FileBackedTasksManager taskManager) {
        //создать первую задачу
        taskManager.createTask(new Task(
                "First Task",
                "First simple task for example",
                1,
                TaskStatus.NEW));

        //создать вторую задачу
        taskManager.createTask(new Task(
                "Second Task",
                "Second simple task for example",
                2,
                TaskStatus.IN_PROGRESS));

        //создать эпик с тремя подзадачами
        taskManager.createEpicTask(new EpicTask(
                "First EpicTask",
                "First Epic task for example",
                3,
                TaskStatus.NEW));
        taskManager.createSubTask(new SubTask(
                "First SubTask",
                "First Subtask for example",
                TaskStatus.IN_PROGRESS,
                3));
        taskManager.createSubTask(new SubTask(
                "Second SubTask",
                "First Subtask for example",
                TaskStatus.NEW,
                3));
        taskManager.createSubTask(new SubTask(
                "Third SubTask",
                "Third Subtask for example",
                TaskStatus.IN_PROGRESS,
                3));
        //создать эпик без подзадач
        taskManager.createEpicTask(new EpicTask(
                "First EpicTask",
                "First Epic task for example",
                7,
                TaskStatus.NEW));

        //запрос задач в разном порядке
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getEpicTaskById(3);
        taskManager.getEpicTaskById(4);
        taskManager.getEpicTaskById(5);
        taskManager.getEpicTaskById(6);
        taskManager.getEpicTaskById(7);
        taskManager.getSubTaskById(3);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(7);
        //вывести историю
        taskManager.getHistory();
        //повторить запрос задач в разном порядке
        taskManager.getEpicTaskById(3);
        taskManager.getSubTaskById(5);
        taskManager.getEpicTaskById(4);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(5);
        taskManager.getEpicTaskById(5);
        taskManager.getEpicTaskById(6);
        taskManager.getEpicTaskById(7);
        taskManager.getSubTaskById(3);
        taskManager.getTaskById(2);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(6);
        taskManager.getTaskById(3);
        taskManager.getSubTaskById(7);
        taskManager.getTaskById(1);
        //вывести историю
        taskManager.getHistory();
    }

    //сохранить данные менеджера в файл
    public void save() throws ManagerSaveException {
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileName))) {
            bWriter.write("id,type,name,status,description,epic");
            bWriter.newLine();
            for (Task task : getStorageTask()) {
                bWriter.write(taskToString(task));
                bWriter.newLine();
            }
            for (EpicTask task : getStorageEpicTask()) {
                bWriter.write(taskToString(task));
                bWriter.newLine();
            }
            for (SubTask task : getStorageSubTask()) {
                bWriter.write(taskToString(task));
                bWriter.newLine();
            }
            bWriter.newLine();
            bWriter.write(HistoryManager.historyToString(storageHistory));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла");
        }
    }

    //загрузить данные из файла
    public void read() throws ManagerSaveException {
        try (BufferedReader bReader = new BufferedReader(new FileReader(fileName))) {
            //прочитать первую строку без использования
            String line = bReader.readLine();
            //читать файл и заполнять менеджеры
            while ((line = bReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    fromString(line);
                }
            }
            //определить последний id в файле и присвоить id в менеджере
            setIdManagerFromFile();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла");
        }
    }

    //определить последний id в файле и присвоить id в менеджере
    private void setIdManagerFromFile() {
        List<Integer> idCollection = new ArrayList<>();
        for (Map.Entry<Integer, Task> id : storageTask.entrySet()) {
            idCollection.add(id.getKey());
        }
        for (Map.Entry<Integer, EpicTask> id : storageEpicTask.entrySet()) {
            idCollection.add(id.getKey());
        }
        for (Map.Entry<Integer, SubTask> id : storageSubTask.entrySet()) {
            idCollection.add(id.getKey());
        }
        Collections.sort(idCollection);
        setId(idCollection.get(idCollection.size()-1)+1);
    }

    //преобразовать строку в Task и историю запросов
    private void fromString(String line) {
        String[] valuesFromFile = line.split(",");
        if ((valuesFromFile[1].equals(TasksType.TASK.toString()))) {
            Task task = new Task(valuesFromFile[2],
                    valuesFromFile[4],
                    (Integer.parseInt(valuesFromFile[0])),
                    getStatus(valuesFromFile[3]));
            createTaskFromFile(task);

        } else if ((valuesFromFile[1].equals(TasksType.SUBTASK.toString()))) {
            SubTask task = new SubTask(valuesFromFile[2],
                    valuesFromFile[4],
                    (Integer.parseInt(valuesFromFile[0])),
                    getStatus(valuesFromFile[3]),
                    (Integer.parseInt(valuesFromFile[5])));
            createSubTaskFromFile(task);

        } else if ((valuesFromFile[1].equals(TasksType.EPICTASK.toString()))) {
            EpicTask task = new EpicTask(valuesFromFile[2],
                    valuesFromFile[4],
                    (Integer.parseInt(valuesFromFile[0])),
                    getStatus(valuesFromFile[3]));
            createEpicTaskFromFile(task);

        } else {
            List<Integer> historyList = HistoryManager.historyFromString(line);
            for (int id : historyList) {
                storageHistory.add(getAnyTask(id));
            }
        }
    }

    //получить статус из строки
    private TaskStatus getStatus(String status) {
        if (status.equals(TaskStatus.NEW.toString())) {
            return TaskStatus.NEW;
        } else if (status.equals(TaskStatus.IN_PROGRESS.toString())) {
            return TaskStatus.IN_PROGRESS;
        } else {
            return TaskStatus.DONE;
        }
    }

    //создать Task из файла со своим id
    public void createEpicTaskFromFile(EpicTask task) {
        super.setId(task.getId());
        super.createEpicTask(task);
    }
    //создать Task из файла со своим id
    public void createTaskFromFile(Task task) {
        super.setId(task.getId());
        super.createTask(task);
    }

    //создать Task из файла со своим id
    public void createSubTaskFromFile(SubTask task) {
        super.setId(task.getId());
        super.createSubTask(task);
    }

    //перевести Task в строку
    private String taskToString(Task task) {
        return String.format(
                "%s,%s,%s,%s,%s,%s",
                task.getId(),
                (task instanceof SubTask ? TasksType.SUBTASK
                        : task instanceof EpicTask ? TasksType.EPICTASK : TasksType.TASK),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                getEpicId(task)
        );
    }

    //определить принадлежность задачи для сохранения в файл
    private String getEpicId(Task task) {
        if (task instanceof SubTask) {
            return Integer.toString(((SubTask) task).getIdEpicTask());
        }
        return "[null]";
    }

    //получить Task по id из любого хранилища
    private Task getAnyTask(Integer id) {
        if (storageTask.containsKey(id)) {
            return storageTask.get(id);
        } else if (storageEpicTask.containsKey(id)) {
            return storageEpicTask.get(id);
        } else if (storageSubTask.containsKey(id)) {
            return storageSubTask.get(id);
        }
        return null;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createSubTask(SubTask task) {
        super.createSubTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createEpicTask(EpicTask task) {
        super.createEpicTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEpicTask(EpicTask task) {
        super.updateEpicTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubTask(SubTask task) {
       super.updateSubTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Task getTaskById(int idSearch) {
        Task taskToReturn = super.getTaskById(idSearch);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return taskToReturn;
    }

    @Override
    public EpicTask getEpicTaskById (int idSearch) {
        EpicTask taskToReturn = super.getEpicTaskById(idSearch);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return taskToReturn;
    }


    @Override
    public SubTask getSubTaskById (int idSearch) {
        SubTask taskToReturn = super.getSubTaskById(idSearch);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return taskToReturn;
    }

}
