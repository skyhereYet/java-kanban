package Manager;

import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends Task {
    FileBackedTasksManager fileBackedTasksManager1;
    FileBackedTasksManager fileBackedTasksManager2;
    @BeforeEach
    void createManagers() {
        fileBackedTasksManager1 = new FileBackedTasksManager(new File("Resources\\","kanbanTEST.csv"));
        fileBackedTasksManager1.createTask(new Task(
                "First Task",
                "First simple task for example",
                1,
                TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(5)
        ));

        //создать вторую задачу
        fileBackedTasksManager1.createTask(new Task(
                "Second Task",
                "Second simple task for example",
                2,
                TaskStatus.IN_PROGRESS,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,10,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(35)
        ));

        //создать эпик с тремя подзадачами
        fileBackedTasksManager1.createEpicTask(new EpicTask(
                "First EpicTask",
                "First Epic task for example",
                3,
                TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,12,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(10)
        ));
        fileBackedTasksManager1.createSubTask(new SubTask(
                "First SubTask",
                "First Subtask for example",
                0,
                TaskStatus.IN_PROGRESS,
                3,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,13,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(35)
        ));
        fileBackedTasksManager1.createSubTask(new SubTask(
                "Second SubTask",
                "Second Subtask for example",
                0,
                TaskStatus.IN_PROGRESS,
                3,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,14,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(30)
        ));
        fileBackedTasksManager1.createSubTask(new SubTask(
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
        fileBackedTasksManager1.createEpicTask(new EpicTask(
                "Second EpicTask",
                "First Epic task for example",
                3,
                TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,16,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(10)
        ));
    }

    @Test
    void shouldLoadFromFileWhenFileExistAndHistoryIsEmpty() throws ManagerSaveException {
        fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(
                new File("Resources\\","kanbanTEST.csv"));
        assertEquals(fileBackedTasksManager1.storageTask.size(), fileBackedTasksManager2.storageTask.size(),
                "Кол-во Task задач не равно");
        assertEquals(fileBackedTasksManager1.storageEpicTask.size(), fileBackedTasksManager2.storageEpicTask.size(),
                "Кол-во Epic задач не равно");
        assertEquals(fileBackedTasksManager1.storageSubTask.size(), fileBackedTasksManager2.storageSubTask.size(),
                "Кол-во Sub задач не равно");
        assertEquals(fileBackedTasksManager1.storageTaskByTime.size(), fileBackedTasksManager2.storageTaskByTime.size(),
                "Кол-во задач в сортированном списке не равно");
        for (Task task1 : fileBackedTasksManager1.getStorageTask()) {
            assertTrue(fileBackedTasksManager2.getStorageTask().contains(task1), "Похоже не всё загрузилось");
        }
        for (EpicTask task1 : fileBackedTasksManager1.getStorageEpicTask()) {
            assertTrue(fileBackedTasksManager2.getStorageEpicTask().contains(task1), "Похоже не всё загрузилось");
        }
        for (SubTask task1 : fileBackedTasksManager1.getStorageSubTask()) {
            assertTrue(fileBackedTasksManager2.getStorageSubTask().contains(task1), "Похоже не всё загрузилось");
        }
        for (Task task1 : fileBackedTasksManager1.getPrioritizedTasks()) {
            assertTrue(fileBackedTasksManager2.getPrioritizedTasks().contains(task1), "Похоже не всё загрузилось");
        }
        assertEquals(fileBackedTasksManager1.getHistory(), fileBackedTasksManager2.getHistory());
    }

    @Test
    void shouldLoadFromFileWhenFileExistAndHistoryIsNotEmpty() throws ManagerSaveException {
        fileBackedTasksManager1.getTaskById(1);
        fileBackedTasksManager1.getTaskById(2);
        fileBackedTasksManager1.getEpicTaskById(3);
        fileBackedTasksManager1.getEpicTaskById(7);
        fileBackedTasksManager1.getSubTaskById(4);
        fileBackedTasksManager1.getSubTaskById(5);
        fileBackedTasksManager1.getSubTaskById(6);
        fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(
                new File("Resources\\","kanbanTEST.csv"));
        assertEquals(fileBackedTasksManager1.storageTask.size(), fileBackedTasksManager2.storageTask.size(),
                "Кол-во Task задач не равно");
        assertEquals(fileBackedTasksManager1.storageEpicTask.size(), fileBackedTasksManager2.storageEpicTask.size(),
                "Кол-во Epic задач не равно");
        assertEquals(fileBackedTasksManager1.storageSubTask.size(), fileBackedTasksManager2.storageSubTask.size(),
                "Кол-во Sub задач не равно");
        assertEquals(fileBackedTasksManager1.storageTaskByTime.size(), fileBackedTasksManager2.storageTaskByTime.size(),
                "Кол-во задач в сортированном списке не равно");
        for (Task task1 : fileBackedTasksManager1.getStorageTask()) {
            assertTrue(fileBackedTasksManager2.getStorageTask().contains(task1), "Похоже не всё загрузилось");
        }
        for (EpicTask task1 : fileBackedTasksManager1.getStorageEpicTask()) {
            assertTrue(fileBackedTasksManager2.getStorageEpicTask().contains(task1), "Похоже не всё загрузилось");
        }
        for (SubTask task1 : fileBackedTasksManager1.getStorageSubTask()) {
            assertTrue(fileBackedTasksManager2.getStorageSubTask().contains(task1), "Похоже не всё загрузилось");
        }
        for (Task task1 : fileBackedTasksManager1.getPrioritizedTasks()) {
            assertTrue(fileBackedTasksManager2.getPrioritizedTasks().contains(task1), "Похоже не всё загрузилось");
        }
        for (int key = 0; key < fileBackedTasksManager1.getHistory().size(); key++) {
            assertEquals(fileBackedTasksManager1.getHistory().get(key), fileBackedTasksManager2.getHistory().get(key));
        }
    }

    @Test
    void shouldLoadFromFileWhenFileEmpty() throws ManagerSaveException {
        File file = new File("Resources\\","kanbanTESTempty.csv");
        assertThrows(IndexOutOfBoundsException.class, () -> FileBackedTasksManager.loadFromFile(file),
                "Оно загрузилось о.О");
    }

    @Test
    void shouldLoadFromFileWhenFileNotExist() throws ManagerSaveException {
        File file = new File("Resources\\","Test.csv");
        assertThrows(ManagerSaveException.class, () -> FileBackedTasksManager.loadFromFile(file),
                "Оно загрузилось о.О");
    }
}