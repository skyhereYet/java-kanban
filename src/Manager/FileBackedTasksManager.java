package Manager;
import Tasks.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File fileName;

    public FileBackedTasksManager(File fileName) {
        this.fileName=fileName;
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager1;
        FileBackedTasksManager fileBackedTasksManager2;
        try {
            fileBackedTasksManager1 = new FileBackedTasksManager(new File("Resources\\","kanbanDB.csv"));
            makeTest(fileBackedTasksManager1);
            fileBackedTasksManager1.save();

            fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(new File("Resources\\",
                    "kanbanDB.csv"));
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
            for (Task task : fileBackedTasksManager1.getStorageEpicTask()) {
                System.out.println(task);
            }
            System.out.println();
            //вывести EpicTask со второго менеджера
            System.out.println("EpicTask второго менеджера: ");
            for (Task task : fileBackedTasksManager2.getStorageEpicTask()) {
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
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void makeTest(FileBackedTasksManager taskManager) {
        //создать первую задачу
        taskManager.createTask(new Task(
                "First Task",
                "First simple task for example",
                1,
                TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(5)
                ));

        //создать вторую задачу
        taskManager.createTask(new Task(
                "Second Task",
                "Second simple task for example",
                2,
                TaskStatus.IN_PROGRESS,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,10,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(35)
                ));

        //создать эпик с тремя подзадачами
        taskManager.createEpicTask(new EpicTask(
                "First EpicTask",
                "First Epic task for example",
                3,
                TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,12,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(10)
                ));
        taskManager.createSubTask(new SubTask(
                "First SubTask",
                "First Subtask for example",
                0,
                TaskStatus.IN_PROGRESS,
                3,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,13,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(35)
                ));
        taskManager.createSubTask(new SubTask(
                "Second SubTask",
                "Second Subtask for example",
                0,
                TaskStatus.IN_PROGRESS,
                3,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,14,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(30)
                ));
        taskManager.createSubTask(new SubTask(
                "Third SubTask",
                "Third Subtask for example",
                0,
                TaskStatus.IN_PROGRESS,
                3,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,15,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(35)
                ));
        //создать эпик без подзадач
        taskManager.createEpicTask(new EpicTask(
                "Second EpicTask",
                "First Epic task for example",
                3,
                TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,16,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(10)
                ));

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
    private void save() throws ManagerSaveException {
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileName))) {
            bWriter.write("id,type,name,status,description,epic,startTime,duration");
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
            bWriter.write(HistoryUtils.historyToString(storageHistory));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла");
        }
    }

    //загрузить данные из файла
    public static FileBackedTasksManager loadFromFile(File fileName) throws ManagerSaveException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileName);
        try (BufferedReader bReader = new BufferedReader(new FileReader(fileName))) {
            String line = "";
            while (bReader.ready()) {
                line = bReader.readLine();
                if (line.equals("id,type,name,status,description,epic,startTime,duration")) {
                    continue;
                }
                /*по замечанию - "ты здесь останавливаешься на пустой строке, которая отделяет задачи от истории
                просмотров, но последнюю строку (где лежит история) в метод fromString() не отдаешь"

                не согласен, последняя строка всё равно попадает в метод fromString (история загружается полностью,
                все данные соответствуют первой версии менеджера), пока bReader.ready() дает true,
                таким образом избегаем вероятного исключения при направлении пустой строки в метод fromString
                и упрощаем обработку исключения
                */

                if (!line.isEmpty()) {
                    fileBackedTasksManager.fromString(line);
                }
            }
            //определить последний id в файле и присвоить id в менеджере
            fileBackedTasksManager.setIdManagerFromFile();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке файла");
        }
        return fileBackedTasksManager;
    }

    //определить последний id в файле и присвоить id в менеджере
    private void setIdManagerFromFile() {
        List<Integer> idCollection = new ArrayList<>();
        for (Integer id : storageTask.keySet()) {
            idCollection.add(id);
        }
        for (Integer id : storageEpicTask.keySet()) {
            idCollection.add(id);
        }
        for (Integer id : storageSubTask.keySet()) {
            idCollection.add(id);
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
                    getStatus(valuesFromFile[3]),
                    ZonedDateTime.parse(valuesFromFile[6]),
                    Duration.parse(valuesFromFile[7])
                    );
            createTaskFromFile(task);

        } else if ((valuesFromFile[1].equals(TasksType.SUBTASK.toString()))) {
            SubTask task = new SubTask(valuesFromFile[2],
                    valuesFromFile[4],
                    (Integer.parseInt(valuesFromFile[0])),
                    getStatus(valuesFromFile[3]),
                    (Integer.parseInt(valuesFromFile[5])),
                    ZonedDateTime.parse(valuesFromFile[6]),
                    Duration.parse(valuesFromFile[7])
                    );
            createSubTaskFromFile(task);

        } else if ((valuesFromFile[1].equals(TasksType.EPICTASK.toString()))) {
            EpicTask task = new EpicTask(valuesFromFile[2],
                    valuesFromFile[4],
                    (Integer.parseInt(valuesFromFile[0])),
                    getStatus(valuesFromFile[3]),
                    ZonedDateTime.parse(valuesFromFile[6]),
                    Duration.parse(valuesFromFile[7])
                    );
            createEpicTaskFromFile(task);

        } else {
            List<Integer> historyList = HistoryUtils.historyFromString(line);
            for (int id : historyList) {
                try {
                    storageHistory.add(getAnyTask(id));
                } catch (ManagerSaveException e) {
                    System.out.println(e.getMessage());
                } finally {
                    continue;
                }
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
                "%s,%s,%s,%s,%s,%s,%s,%s",
                task.getId(),
                (task instanceof SubTask ? TasksType.SUBTASK
                        : task instanceof EpicTask ? TasksType.EPICTASK : TasksType.TASK),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                getEpicId(task),
                task.getStartTime(),
                task.getDuration()
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
    private Task getAnyTask(Integer id) throws ManagerSaveException {
            try {
                if (storageTask.containsKey(id)) {
                    return storageTask.get(id);
                } else if (storageEpicTask.containsKey(id)) {
                    return storageEpicTask.get(id);
                } else {
                    return storageSubTask.get(id);
                }
            } catch (NullPointerException e) {
                throw new ManagerSaveException("Ошибка чтения истории");
            }

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
    public void updateEpicTask(EpicTask updateEpicTask) {
        super.updateEpicTask(updateEpicTask);
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
