package Manager;

import Custom_Exception.KVTaskServerException;
import Server.KVServer;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends Task {


    HttpTaskManager httpTaskManager1;
    HttpTaskManager httpTaskManager2;
    KVServer kvServer;

    @BeforeEach
    void createManagers() throws IOException, KVTaskServerException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskManager1 = (HttpTaskManager) Managers.getDefault("http://localhost:8078/", false);

        httpTaskManager1.createTask(new Task(
                "First Task",
                "First simple task for example",
                1,
                TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(5)
        ));

        //создать вторую задачу
        httpTaskManager1.createTask(new Task(
                "Second Task",
                "Second simple task for example",
                2,
                TaskStatus.IN_PROGRESS,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,10,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(35)
        ));

        //создать эпик с тремя подзадачами
        httpTaskManager1.createEpicTask(new EpicTask(
                "First EpicTask",
                "First Epic task for example",
                3,
                TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,12,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(10)
        ));
        httpTaskManager1.createSubTask(new SubTask(
                "First SubTask",
                "First Subtask for example",
                0,
                TaskStatus.IN_PROGRESS,
                3,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,13,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(35)
        ));
        httpTaskManager1.createSubTask(new SubTask(
                "Second SubTask",
                "Second Subtask for example",
                0,
                TaskStatus.IN_PROGRESS,
                3,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,14,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(30)
        ));
        httpTaskManager1.createSubTask(new SubTask(
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
        httpTaskManager1.createEpicTask(new EpicTask(
                "Second EpicTask",
                "First Epic task for example",
                3,
                TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,16,25),
                        ZoneId.of("Europe/Moscow")),
                Duration.ofMinutes(10)
        ));
        httpTaskManager2 = (HttpTaskManager) Managers.getDefault("http://localhost:8078/", true);
    }

    @AfterEach
    void closeManagers() {
        kvServer.stop();
    }

    @Test
    void shouldLoadFromKVServer() {
        for (Task task : httpTaskManager1.getStorageTask()) {
            assertEquals(task, httpTaskManager2.getTaskById(task.getId()));
        }
        for (Task task : httpTaskManager1.getStorageSubTask()) {
            assertEquals(task, httpTaskManager2.getSubTaskById(task.getId()));
        }
        for (Task task : httpTaskManager1.getStorageEpicTask()) {
            assertEquals(task, httpTaskManager2.getEpicTaskById(task.getId()));
        }
    }

    @Test
    void save() {
        Gson GSON = Managers.getGson();
        try {
            for (Task task : httpTaskManager1.getStorageTask()) {
                assertEquals(task, GSON.fromJson(httpTaskManager1.kvTaskClient.load(String.valueOf(task.getId())),
                        Task.class));
            }
            for (SubTask task : httpTaskManager1.getStorageSubTask()) {
                assertEquals(task, GSON.fromJson(httpTaskManager1.kvTaskClient.load(String.valueOf(task.getId())),
                        SubTask.class));
            }
            for (EpicTask task : httpTaskManager1.getStorageEpicTask()) {
                assertEquals(task, GSON.fromJson(httpTaskManager1.kvTaskClient.load(String.valueOf(task.getId())),
                        EpicTask.class));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}