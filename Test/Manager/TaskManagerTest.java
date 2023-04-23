package Manager;

import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

abstract class TaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    void createManager() {
        taskManager = taskManager = Managers.getDefault();
    }

    @AfterEach
    void tear() {
        //прибрать за собой
    }

    @Test
    void shouldIncreaseIdAfterGetId() {
        Task task = new Task("NameTask", "Description", 0, TaskStatus.NEW);
        taskManager.createTask(task);
        assertEquals(1, task.getId());
    }

    @Test
    void shouldCreateTaskWithoutError() {
        Task task = new Task("NameTask", "Description", 0, TaskStatus.NEW);
        taskManager.createTask(task);
        assertEquals(task, taskManager.getStorageTask().get(0));
        assertFalse(taskManager.getStorageTask().isEmpty());
        assertTrue(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertFalse(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldCreateNullTask() {
        Task task = null;
        assertThrows(NullPointerException.class, () -> {
            taskManager.createTask(task);
        });
        assertTrue(taskManager.getStorageTask().isEmpty());
        assertTrue(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldCreateEpicTaskWithoutError() {
        EpicTask epictask = new EpicTask("NameTask", "Description", 0, TaskStatus.NEW);
        taskManager.createEpicTask(epictask);
        assertEquals(epictask, taskManager.getStorageEpicTask().get(0));
        assertTrue(taskManager.getStorageTask().isEmpty());
        assertFalse(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldCreateNullEpicTask() {
        EpicTask epictask = null;
        assertThrows(NullPointerException.class, () -> {
            taskManager.createEpicTask(epictask);
        });
        assertTrue(taskManager.getStorageTask().isEmpty());
        assertTrue(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldCreateSubTaskWithoutError() {
        EpicTask epictask = new EpicTask("NameEpicTask", "Description", 0, TaskStatus.NEW);
        SubTask subtask = new SubTask("NameSubTask", "Description", 0, TaskStatus.NEW, 1,
                            ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                            ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createEpicTask(epictask);
        taskManager.createSubTask(subtask);
        assertEquals(epictask, taskManager.getStorageEpicTask().get(0));
        assertEquals(subtask, taskManager.getStorageSubTask().get(0));
        assertTrue(taskManager.getStorageTask().isEmpty());
        assertFalse(taskManager.getStorageEpicTask().isEmpty());
        assertFalse(taskManager.getStorageSubTask().isEmpty());
        assertFalse(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldCreateSubTaskWithoutEpic() {
        SubTask subtask = new SubTask("NameSubTask", "Description", 0, TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        assertThrows(IllegalArgumentException.class, () -> taskManager.createSubTask(subtask));
        assertTrue(taskManager.getStorageTask().isEmpty());
        assertTrue(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldCreateNullSubTask() {
        SubTask subtask = null;
        assertThrows(NullPointerException.class, () -> {
            taskManager.createSubTask(subtask);
        });
        assertTrue(taskManager.getStorageTask().isEmpty());
        assertTrue(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldUpdateTaskWithoutError() {
        Task task = new Task("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));

        Task updateTask = new Task("Update", "Update", 1, TaskStatus.IN_PROGRESS,
                ZonedDateTime.of(LocalDateTime.of(2024, 05, 9,10,12),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(05));

        taskManager.createTask(task);
        assertEquals(task, taskManager.getStorageTask().get(0));
        taskManager.updateTask(updateTask);
        assertEquals(updateTask, taskManager.getStorageTask().get(0));
        assertEquals(1, taskManager.getStorageTask().size());
        assertFalse(taskManager.getStorageTask().isEmpty());
        assertTrue(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertFalse(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldUpdateTaskNull() {
        Task task = new Task("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));

        Task updateTask = null;

        taskManager.createTask(task);
        assertEquals(task, taskManager.getStorageTask().get(0));
        assertThrows(NullPointerException.class, () -> {
            taskManager.updateTask(updateTask);
        });
        assertFalse(taskManager.getStorageTask().isEmpty());
        assertTrue(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertFalse(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldUpdateEpicTaskWithoutError() {
        EpicTask task = new EpicTask("NameTask", "Description", 1, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));

        EpicTask updateTask = new EpicTask("Update", "Update", 1, TaskStatus.IN_PROGRESS,
                ZonedDateTime.of(LocalDateTime.of(2024, 05, 9,10,12),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(05));

        taskManager.createEpicTask(task);
        assertEquals(task, taskManager.getStorageEpicTask().get(0));
        taskManager.updateEpicTask(updateTask);
        assertEquals(updateTask, taskManager.getStorageEpicTask().get(0));
        assertEquals(1, taskManager.getStorageEpicTask().size());
        assertTrue(taskManager.getStorageTask().isEmpty());
        assertFalse(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldUpdateSubTaskWithoutError() {
        EpicTask epictask = new EpicTask("NameEpicTask", "Description", 1, TaskStatus.NEW);
        SubTask subtask = new SubTask("NameSubTask", "Description", 2, TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));

        SubTask updateSubtask = new SubTask("UpdateSubTask", "UpdateSubTask", 2,
                TaskStatus.IN_PROGRESS, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(05));

        taskManager.createEpicTask(epictask);
        taskManager.createSubTask(subtask);
        assertEquals(epictask, taskManager.getStorageEpicTask().get(0));
        assertEquals(subtask, taskManager.getStorageSubTask().get(0));
        taskManager.updateSubTask(updateSubtask);
        assertEquals(epictask, taskManager.getStorageEpicTask().get(0));
        assertEquals(updateSubtask, taskManager.getStorageSubTask().get(0));
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getStorageEpicTask().get(0).getStatus());
        assertEquals(updateSubtask.getStartTime(), taskManager.getStorageEpicTask().get(0).getStartTime());
        assertEquals(updateSubtask.getDuration(), taskManager.getStorageEpicTask().get(0).getDuration());
        assertTrue(taskManager.getStorageTask().isEmpty());
        assertFalse(taskManager.getStorageEpicTask().isEmpty());
        assertFalse(taskManager.getStorageSubTask().isEmpty());
        assertFalse(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldChangeStatusEpicTestNEW() {
        EpicTask epictask = new EpicTask("NameEpicTask", "Description", 1, TaskStatus.DONE);
        SubTask subtask = new SubTask("NameSubTask", "Description", 2, TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));

        taskManager.createEpicTask(epictask);
        taskManager.createSubTask(subtask);
        assertEquals(TaskStatus.NEW, taskManager.getStorageEpicTask().get(0).getStatus(),
                "Статус Epictask не изменен на NEW");
    }

    @Test
    void shouldChangeStatusEpicTestIN_PROGRESS() {
        EpicTask epictask = new EpicTask("NameEpicTask", "Description", 1, TaskStatus.DONE);
        SubTask subtask1 = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        SubTask subtask2 = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.IN_PROGRESS, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,21,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createEpicTask(epictask);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getStorageEpicTask().get(0).getStatus(),
                "Статус Epictask не изменен на IN_PROGRESS");
    }

    @Test
    void shouldChangeStatusEpicTestDONE() {
        EpicTask epictask = new EpicTask("NameEpicTask", "Description", 1, TaskStatus.NEW);
        SubTask subtask1 = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        SubTask subtask2 = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.DONE, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createEpicTask(epictask);
        taskManager.createSubTask(subtask1);
        assertEquals(TaskStatus.NEW, taskManager.getStorageEpicTask().get(0).getStatus(),
                "Статус Epictask не изменен на IN_PROGRESS");
        taskManager.deleteAnyTaskById(2);
        taskManager.createSubTask(subtask2);
        assertEquals(TaskStatus.DONE, taskManager.getStorageEpicTask().get(0).getStatus(),
                "Статус Epictask не изменен на DONE");
    }

    @Test
    void shouldIncrementId() {
        taskManager.createId();
        assertEquals(2, taskManager.getId(), "id не инкрементирован");
    }

    @Test
    void shouldGetStorageTaskIsEmpty() {
        assertTrue(taskManager.getStorageTask().isEmpty(), "Хранилище Task не пустое");
    }

    @Test
    void shouldGetStorageTaskIsNotEmpty() {
        Task task = new Task("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createTask(task);
        assertFalse(taskManager.getStorageTask().isEmpty(), "Хранилище Task пустое");
    }

    @Test
    void shouldGetStorageEpicTaskIsEmpty() {
        assertTrue(taskManager.getStorageEpicTask().isEmpty(), "Хранилище EpicTask не пустое");
    }

    @Test
    void shouldGetStorageEpicTaskIsNotEmpty() {
        EpicTask epictask = new EpicTask("NameEpicTask", "Description", 1, TaskStatus.NEW);
        taskManager.createEpicTask(epictask);
        assertFalse(taskManager.getStorageEpicTask().isEmpty(), "Хранилище EpicTask пустое");
    }

    @Test
    void shouldGetStorageSubTaskIsEmpty() {
        assertTrue(taskManager.getStorageSubTask().isEmpty(), "Хранилище SubTask не пустое");
    }

    @Test
    void shouldGetStorageSubTaskIsNotEmpty() {
        EpicTask epictask = new EpicTask("NameEpicTask", "Description", 1, TaskStatus.NEW);
        SubTask subtask = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createEpicTask(epictask);
        taskManager.createSubTask(subtask);
        assertFalse(taskManager.getStorageSubTask().isEmpty(), "Хранилище SubTask пустое");
    }

    @Test
    void shouldEraseStorageTaskWhenEmpty() {
        taskManager.eraseStorageTask();
        assertTrue(taskManager.getStorageTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldEraseStorageTaskWhenIsNotEmpty() {
        Task task = new Task("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createTask(task);
        assertFalse(taskManager.getStorageTask().isEmpty());
        assertFalse(taskManager.getPrioritizedTasks().isEmpty());
        taskManager.eraseStorageTask();
        assertTrue(taskManager.getStorageTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldEraseStorageEpicTaskWhenEmpty() {
        taskManager.eraseStorageEpicTask();
        assertTrue(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldEraseStorageEpicTaskWhenIsNotEmpty() {
        EpicTask epictask = new EpicTask("NameEpicTask", "Description", 1, TaskStatus.NEW);
        SubTask subtask = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createEpicTask(epictask);
        taskManager.createSubTask(subtask);
        assertFalse(taskManager.getStorageEpicTask().isEmpty());
        assertFalse(taskManager.getStorageSubTask().isEmpty());
        assertFalse(taskManager.getPrioritizedTasks().isEmpty());
        taskManager.eraseStorageEpicTask();
        assertTrue(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldEraseStorageSubTaskWhenEmpty() {
        taskManager.eraseStorageSubTask();
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    void shouldEraseStorageSubTaskWhenIsNotEmpty() {
        EpicTask epictask = new EpicTask("NameEpicTask", "Description", 1, TaskStatus.NEW);
        SubTask subtask = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createEpicTask(epictask);
        taskManager.createSubTask(subtask);
        assertFalse(taskManager.getStorageEpicTask().isEmpty());
        assertFalse(taskManager.getStorageSubTask().isEmpty());
        assertFalse(taskManager.getPrioritizedTasks().isEmpty());
        taskManager.eraseStorageSubTask();
        assertFalse(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }
    @Test
    void shouldGetTaskByIdWhenIdNull() {
        assertThrows(NumberFormatException.class, () -> taskManager.getTaskById(Integer.parseInt(null)));
    }

    @Test
    void shouldGetTaskByIdWhenIdExist() {
        Task task = new Task("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createTask(task);
        assertEquals(task, taskManager.getTaskById(1));
    }

    @Test
    void shouldGetTaskByIdWhenIdNotExist() {
        assertEquals(null, taskManager.getTaskById(2));
    }

    @Test
    void shouldGetEpicTaskByIdWhenIdNull() {
        assertThrows(NumberFormatException.class, () -> taskManager.getEpicTaskById(Integer.parseInt(null)));
    }

    @Test
    void shouldGetEpicTaskByIdWhenIdExist() {
        EpicTask epicTask = new EpicTask("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createEpicTask(epicTask);
        assertEquals(epicTask, taskManager.getEpicTaskById(1));
    }

    @Test
    void shouldGetEpicTaskByIdWhenIdNotExist() {
        assertEquals(null, taskManager.getTaskById(2));
    }

    @Test
    void shouldGetSubTaskByIdWhenIdNull() {
        assertThrows(NumberFormatException.class, () -> taskManager.getSubTaskById(Integer.parseInt(null)));
    }

    @Test
    void shouldGetSubTaskByIdWhenIdIsNotExist() {
        EpicTask epicTask = new EpicTask("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        SubTask subtask = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subtask);
        assertEquals(null, taskManager.getSubTaskById(1));
    }

    @Test
    void shoulDeleteAnyTaskByIdWhenIdNull() {
        assertThrows(NumberFormatException.class, () -> taskManager.deleteAnyTaskById(Integer.parseInt(null)));
    }

    @Test
    void shoulDeleteAnyTaskByIdWhenIdNotExist() {
        assertDoesNotThrow(() -> taskManager.deleteAnyTaskById(1));
    }

    @Test
    void shoulDeleteAnyTaskByIdWhenIdExist() {
        Task task = new Task("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        EpicTask epictask = new EpicTask("NameTask", "Description", 0, TaskStatus.NEW);
        SubTask subtask = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.NEW, 2,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,21,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createTask(task);
        taskManager.createEpicTask(epictask);
        taskManager.createSubTask(subtask);
        taskManager.deleteAnyTaskById(3);
        assertFalse(taskManager.getStorageSubTask().contains(subtask));
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertFalse(taskManager.getPrioritizedTasks().contains(subtask));
        taskManager.createSubTask(subtask);
        taskManager.deleteAnyTaskById(2);
        assertFalse(taskManager.getStorageEpicTask().contains(epictask));
        assertFalse(taskManager.getStorageSubTask().contains(subtask));
        assertTrue(taskManager.getStorageEpicTask().isEmpty());
        assertTrue(taskManager.getStorageSubTask().isEmpty());
        assertFalse(taskManager.getPrioritizedTasks().contains(subtask));
        taskManager.deleteAnyTaskById(1);
        assertTrue(taskManager.getStorageTask().isEmpty());
        assertFalse(taskManager.getPrioritizedTasks().contains(task));
    }

    @Test
    void shouldGetSubTaskByEpicIdWhenNull() {
        assertThrows(NumberFormatException.class, () -> taskManager.getSubTaskByEpicId(Integer.parseInt(null)));
    }

    @Test
    void shouldGetSubTaskByEpicIdWhenNotExist() {
        assertNull(taskManager.getSubTaskByEpicId(1));
    }

    @Test
    void shouldGetSubTaskByEpicIdWhenExist() {
        EpicTask epictask = new EpicTask("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        SubTask subtask = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.NEW, 1,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        taskManager.createEpicTask(epictask);
        taskManager.createSubTask(subtask);
        assertEquals(List.of(subtask), taskManager.getSubTaskByEpicId(1));
    }

    @Test
    void shouldGetHistoryWhenEmpty() {
        assertTrue(taskManager.getHistory().isEmpty());
    }

    Task getNewTask() {
        return new Task("NameTask", "Description", 0, TaskStatus.NEW);
    }

    @Test
    void shouldGetHistoryWhenHistoryExist() {
        Task task1 = getNewTask();
        Task task2 = getNewTask();
        Task task3 = getNewTask();
        Task task4 = getNewTask();
        Task task5 = getNewTask();
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getTaskById(4);
        taskManager.getTaskById(5);
        assertEquals(List.of(task1, task2, task3, task4, task5), taskManager.getHistory());
    }

    @Test
    void shouldGetHistoryWhenHistoryExistWithDublicate() {
        Task task1 = getNewTask();
        Task task2 = getNewTask();
        Task task3 = getNewTask();
        Task task4 = getNewTask();
        Task task5 = getNewTask();
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.getTaskById(1);
        taskManager.getTaskById(5);
        taskManager.getTaskById(2);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getTaskById(4);
        taskManager.getTaskById(2);
        taskManager.getTaskById(5);
        taskManager.getTaskById(1);
        assertEquals(List.of(task3, task4, task2, task5, task1), taskManager.getHistory());
    }

    @Test
    void shouldGetHistoryWhenHistoryAfterRemoveTaskIntermediate() {
        Task task1 = getNewTask();
        Task task2 = getNewTask();
        Task task3 = getNewTask();
        Task task4 = getNewTask();
        Task task5 = getNewTask();
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getTaskById(4);
        taskManager.getTaskById(5);
        taskManager.deleteAnyTaskById(3);
        assertEquals(List.of(task1, task2, task4, task5), taskManager.getHistory());
    }

    @Test
    void shouldGetHistoryWhenHistoryAfterRemoveFirstTask() {
        Task task1 = getNewTask();
        Task task2 = getNewTask();
        Task task3 = getNewTask();
        Task task4 = getNewTask();
        Task task5 = getNewTask();
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getTaskById(4);
        taskManager.getTaskById(5);
        taskManager.deleteAnyTaskById(1);
        assertEquals(List.of(task2, task3, task4, task5), taskManager.getHistory());
    }

    @Test
    void shouldGetHistoryWhenHistoryAfterRemoveLastTask() {
        Task task1 = getNewTask();
        Task task2 = getNewTask();
        Task task3 = getNewTask();
        Task task4 = getNewTask();
        Task task5 = getNewTask();
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        taskManager.getTaskById(4);
        taskManager.getTaskById(5);
        taskManager.deleteAnyTaskById(5);
        assertEquals(List.of(task1, task2, task3, task4), taskManager.getHistory());
    }

    @Test
    void shouldValidationTasksWhenNotOverlaps() {
        Task task1 = new Task("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        //startTime - 20:25
        //endTime - 21:00

        Task task2 = new Task("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,21,00),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(30));
        //startTime - 21:00
        //endTime - 21:30

        EpicTask epictask = new EpicTask("NameTask", "Description", 0, TaskStatus.NEW);

        SubTask subtask = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.NEW, 3,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,10,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        //startTime - 10:25
        //endTime - 11:00

        assertDoesNotThrow(() ->taskManager.createTask(task1), "Возникло исключение");
        assertDoesNotThrow(() ->taskManager.createTask(task2), "Возникло исключение");
        assertDoesNotThrow(() ->taskManager.createEpicTask(epictask), "Возникло исключение");
        assertDoesNotThrow(() ->taskManager.createSubTask(subtask), "Возникло исключение");
    }

    @Test
    void shouldValidationTasksWhenOverlaps() {
        Task task1 = new Task("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(36));
        //startTime - 20:25
        //endTime - 21:01

        Task task2 = new Task("NameTask", "Description", 0, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,21,00),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(30));
        //startTime - 21:00
        //endTime - 21:30

        EpicTask epictask = new EpicTask("NameTask", "Description", 0, TaskStatus.NEW);

        SubTask subtask1 = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.NEW, 2,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,10,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        //startTime - 10:25
        //endTime - 11:00

        SubTask subtask2 = new SubTask("NameSubTask", "Description", 2,
                TaskStatus.NEW, 2,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,10,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
        //startTime - 10:25
        //endTime - 11:00

        assertDoesNotThrow(() ->taskManager.createTask(task1), "Возникло исключение");
        assertThrows(ManagerTimeException.class, () -> taskManager.createTask(task2));
        assertDoesNotThrow(() ->taskManager.createEpicTask(epictask), "Возникло исключение");
        assertDoesNotThrow(() ->taskManager.createSubTask(subtask1), "Возникло исключение");
        assertThrows(ManagerTimeException.class, () -> taskManager.createSubTask(subtask2));
    }
}