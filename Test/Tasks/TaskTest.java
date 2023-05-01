package Tasks;

import Manager.FileBackedTasksManager;
import Manager.Managers;
import Manager.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    Task task1 = new Task("Name Task1", "Description", TaskStatus.NEW,
                            ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,20,25),
                            ZoneId.of("Europe/Moscow")), Duration.ofMinutes(35));
                            //startTime - 20:25
                            //endTime - 21:00

    Task task2 = new Task("Name Task2", "Description", TaskStatus.NEW,
                            ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,21,25),
                            ZoneId.of("Europe/Moscow")), Duration.ofMinutes(5));
                            //startTime - 21:25
                            //endTime - 21:30

    Task task3 = new Task("Name Task3", "Description", TaskStatus.NEW,
                            ZonedDateTime.of(LocalDateTime.of(2023, 04, 19,21,28),
                            ZoneId.of("Europe/Moscow")), Duration.ofMinutes(47));
    Task task4 = new Task("NULL4", "Description", TaskStatus.NEW,null, null);
    Task task5 = new Task("NULL5", "Description", TaskStatus.NEW,null, null);
    @Test
    void getAllTime(){
        Comparator<Task> dateTimeComparator = (task1, task2) -> {
                if (task1.getStartTime() == task2.getStartTime()) {
                    return 1;
                } else if (task2.getStartTime() == null) {
                    return -1;
                } else if (task1.getStartTime() == null) {
                    return -1;
                }
                return task1.getStartTime().toLocalTime().compareTo(task2.getStartTime().toLocalTime());
        };
        TreeSet<Task> treeSet = new TreeSet(Comparator.nullsLast(dateTimeComparator));
        treeSet.add(task4);
        treeSet.add(task3);
        treeSet.add(task5);
        treeSet.add(task5);
        treeSet.add(task1);
        treeSet.add(task2);
        treeSet.iterator().forEachRemaining(t -> System.out.println(t));
    }


    @Test
    void shouldOverlapsWhenNotOverlaps() {
        System.out.println(task1.overlaps(task2));
        System.out.println(task2.overlaps(task3));
    }
}