package Tasks;

import Manager.Managers;
import Manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest extends Task {
    TaskManager taskManager;
    EpicTask task;
    int id;
    ZonedDateTime zonedDateTime;

    @BeforeEach
    void createEpicTask() {
        zonedDateTime = ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,
                                                                        25), ZoneId.of("Europe/Moscow"));
        EpicTask epictask = new EpicTask("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager = Managers.getDefault();
        taskManager.createEpicTask(epictask);
        id = taskManager.getStorageEpicTask().get(0).getId();
    }

    @Test
    void testGetStatusWhenSubtaskStorageEmpty() {
        assertEquals(1, id);
        assertTrue(taskManager.getEpicTaskById(id).getStorageSubtask().isEmpty(), "Список подзадач не пуст.");
    }

    @Test
    void testGetStatusWhenAllSubtaskNEW() {
        SubTask subtask1 = new SubTask("First subtask", "Subtask description", 1, TaskStatus.NEW,
                1, zonedDateTime, Duration.ofMinutes(35));
        ZonedDateTime newZonedDateTime = zonedDateTime.plus(Duration.ofMinutes(50));
        SubTask subtask2 = new SubTask("Second subtask", "Subtask description", 1, TaskStatus.NEW,
                1, newZonedDateTime, Duration.ofMinutes(35));
        newZonedDateTime = zonedDateTime.plus(Duration.ofMinutes(150));
        SubTask subtask3 = new SubTask("Third subtask", "Subtask description", 1, TaskStatus.NEW,
                1, newZonedDateTime, Duration.ofMinutes(35));
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        taskManager.createSubTask(subtask3);
        assertEquals(TaskStatus.NEW, taskManager.getEpicTaskById(id).getStatus(),
                        "Статус Epic задачи отличен от NEW. Текущий статус - " + taskManager.getEpicTaskById(id).getStatus());
    }

    @Test
    void testGetStatusWhenAllSubtaskDONE() {
        SubTask subtask1 = new SubTask("First subtask", "Subtask description", 1, TaskStatus.DONE,
                1, zonedDateTime, Duration.ofMinutes(35));
        ZonedDateTime newZonedDateTime = zonedDateTime.plus(Duration.ofMinutes(50));
        SubTask subtask2 = new SubTask("Second subtask", "Subtask description", 1, TaskStatus.DONE,
                1, newZonedDateTime, Duration.ofMinutes(35));
        newZonedDateTime = zonedDateTime.plus(Duration.ofMinutes(150));
        SubTask subtask3 = new SubTask("Third subtask", "Subtask description", 1, TaskStatus.DONE,
                1, newZonedDateTime, Duration.ofMinutes(35));
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        taskManager.createSubTask(subtask3);
        assertEquals(TaskStatus.DONE, taskManager.getEpicTaskById(id).getStatus(),
                "Статус Epic задачи отличен от DONE. Текущий статус - " + taskManager.getEpicTaskById(id).getStatus());
    }

    @Test
    void testGetStatusWhenSomeSubtaskNEWandDONE() {
        SubTask subtask1 = new SubTask("First subtask", "Subtask description", 1, TaskStatus.DONE,
                1, zonedDateTime, Duration.ofMinutes(35));
        ZonedDateTime newZonedDateTime = zonedDateTime.plus(Duration.ofMinutes(50));
        SubTask subtask2 = new SubTask("Second subtask", "Subtask description", 1, TaskStatus.NEW,
                1, newZonedDateTime, Duration.ofMinutes(35));
        newZonedDateTime = zonedDateTime.plus(Duration.ofMinutes(150));
        SubTask subtask3 = new SubTask("Third subtask", "Subtask description", 1, TaskStatus.DONE,
                1, newZonedDateTime, Duration.ofMinutes(35));
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        taskManager.createSubTask(subtask3);
        assertEquals(TaskStatus.NEW, taskManager.getEpicTaskById(id).getStatus(),
                "Статус Epic задачи отличен от NEW. Текущий статус - " + taskManager.getEpicTaskById(id).getStatus());
    }

    @Test
    void testGetStatusWhenSomeSubtaskNEWandIN_PROGRESS() {
        SubTask subtask1 = new SubTask("First subtask", "Subtask description", 1, TaskStatus.NEW,
                1, zonedDateTime, Duration.ofMinutes(35));
        ZonedDateTime newZonedDateTime = zonedDateTime.plus(Duration.ofMinutes(50));
        SubTask subtask2 = new SubTask("Second subtask", "Subtask description", 1, TaskStatus.IN_PROGRESS,
                1, newZonedDateTime, Duration.ofMinutes(35));
        newZonedDateTime = zonedDateTime.plus(Duration.ofMinutes(180));
        SubTask subtask3 = new SubTask("Third subtask", "Subtask description", 1, TaskStatus.DONE,
                1, newZonedDateTime, Duration.ofMinutes(35));
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        taskManager.createSubTask(subtask3);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicTaskById(id).getStatus(),
                "Статус Epic задачи отличен от IN_PROGRESS. Текущий статус - " + taskManager.getEpicTaskById(id).getStatus());
    }

}