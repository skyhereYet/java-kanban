package Manager;

import Tasks.Task;
import Tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest extends Task {
    HistoryManager historyManager;
    int id = 0;

    Task getNewTask() {
        id++;
        return new Task("NameTask", "Description", id, TaskStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                        ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
    }

    @BeforeEach
    void createManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldGetHistoryWhenEmpty() {
        assertEquals(List.of(), historyManager.getHistory());
    }

    @Test
    void shouldGetHistoryWhenHistoryNotEmpty() {
        Task task1 = getNewTask();
        Task task2 = getNewTask();
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(List.of(task1, task2), historyManager.getHistory());
    }

    @Test
    void shouldGetHistoryWhenHistoryAddDublicate() {
        Task task1 = getNewTask();
        Task task2 = getNewTask();
        Task task3 = getNewTask();
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task2);
        historyManager.add(task3);
        assertEquals(List.of(task1, task2, task3), historyManager.getHistory());
    }

    @Test
    void shouldGetHistoryWhenHistoryAfterRemoveFirst() {
        Task task1 = getNewTask();
        Task task2 = getNewTask();
        Task task3 = getNewTask();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task1);
        assertEquals(List.of(task2, task3), historyManager.getHistory());
    }

    @Test
    void shouldGetHistoryWhenHistoryAfterRemoveMiddle() {
        Task task1 = getNewTask();
        Task task2 = getNewTask();
        Task task3 = getNewTask();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2);
        assertEquals(List.of(task1, task3), historyManager.getHistory());
    }

    @Test
    void shouldGetHistoryWhenHistoryAfterRemoveLast() {
        Task task1 = getNewTask();
        Task task2 = getNewTask();
        Task task3 = getNewTask();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task3);
        assertEquals(List.of(task1, task2), historyManager.getHistory());
    }
}