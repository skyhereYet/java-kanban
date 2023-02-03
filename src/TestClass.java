import java.util.HashMap;
import java.util.Scanner;

public class TestClass {
    Scanner scanner = new Scanner(System.in);
    //Простые задачи
    Task taskTest1 = new Task("First Task", "First simple task for example", 1, TaskStatus.NEW);
    Task taskTest2 = new Task("Second Task", "Second simple task for example", 2, TaskStatus.IN_PROGRESS);
    Task taskTest3 = new Task("Third Task", "Third simple task for example", 3, TaskStatus.DONE);
    Task updateTaskTest1 = new Task("Updated Task", "Updated simple task for example", 1, TaskStatus.DONE);
    //Подзадачи
    SubTask subTaskTest1 = new SubTask("First SubTask", "First subtask for example", 4,
            TaskStatus.NEW, 4);
    SubTask subTaskTest2 = new SubTask("Second SubTask", "Second subtask for example", 5,
            TaskStatus.IN_PROGRESS, 5);
    SubTask subTaskTest3 = new SubTask("Third SubTask", "Third subtask for example", 6,
            TaskStatus.DONE, 7);
    SubTask subTaskTest4 = new SubTask("Fourth SubTask", "Fourth subtask for example", 7,
            TaskStatus.NEW, 5);
    SubTask updateSubTaskTest1 = new SubTask("Updated SubTask", "Updated subtask for example", 5,
            TaskStatus.DONE, 4);
    //создаем списки подзадача
    HashMap<Integer, SubTask> testStorageSubtask1 = new HashMap<>();
    HashMap<Integer, SubTask> testStorageSubtask2 = new HashMap<>();
    //Epic задачи
    EpicTask epicTaskTest1 = new EpicTask("First EpicTask", "First epictask for example", 8,
            TaskStatus.NEW, testStorageSubtask1);
    EpicTask epicTaskTest2 = new EpicTask("Second EpicTask", "Second epictask for example", 9,
            TaskStatus.IN_PROGRESS, testStorageSubtask2);
    EpicTask updateEpicTaskTest2 = new EpicTask("Updated EpicTask", "Updated epictask for example", 7,
            TaskStatus.IN_PROGRESS, testStorageSubtask1);

    void setHashMap1(){
        testStorageSubtask1.put(subTaskTest1.getId(), subTaskTest1);
        testStorageSubtask1.put(subTaskTest3.getId(), subTaskTest3);
    }

    void setHashMap2(){
        testStorageSubtask2.put(subTaskTest2.getId(), subTaskTest2);
    }

    void printMenu() {
        // Вывод доступных команд
        System.out.println("______");
        System.out.println("Введите команду");
        System.out.println("1 - Создать задачу Task");
        System.out.println("2 - Создать задачу EpicTask");
        System.out.println("3 - Создать задачу SubTask");
        System.out.println("4 - Обновить задачу Task");
        System.out.println("5 - Обновить задачу EpicTask");
        System.out.println("6 - Обновить задачу SubTask");
        System.out.println("7 - Удалить все задачи Task");
        System.out.println("8 - Удалить все задачи EpicTask");
        System.out.println("9 - Удалить все задачи SubTask");
        System.out.println("10 - Получить список задач Task");
        System.out.println("11 - Получить список задач EpicTask");
        System.out.println("12 - Получить список задач SubTask");
        System.out.println("13 - Получить Task по id");
        System.out.println("14 - Получить EpicTask по id");
        System.out.println("15 - Получить SubTask по id");
        System.out.println("16 - Удалить задачу по id");
        System.out.println("17 - Вывести список SubTask по id EpicTask");
        System.out.println("0 - Выйти из приложения");
    }

    void startTest(Manager manager) {
        while (true) {
            printMenu(); //вывод меню
            int numberMenu = scanner.nextInt();

            if (numberMenu == 1) {
                System.out.println(manager.getStorageTask().keySet());
                manager.createTask(taskTest1);
                manager.createTask(taskTest2);
                manager.createTask(taskTest3);
                System.out.println("Задачи Task добавлены" + manager.getStorageTask().keySet());
                System.out.println("Счетчик id = " + manager.getId());

            } else if (numberMenu == 2) {
                System.out.println(manager.getStorageEpicTask().keySet());
                manager.createEpicTask(epicTaskTest1);
                manager.createEpicTask(epicTaskTest2);
                System.out.println("Задачи EpicTask добавлены" + manager.getStorageEpicTask().keySet());
                System.out.println("Счетчик id = " + manager.getId());

            } else if (numberMenu == 3) {
                System.out.println(manager.getStorageSubTask().keySet());
                manager.createSubTask(subTaskTest4);
                System.out.println("Задачи SubTask добавлены" + manager.getStorageSubTask().keySet());
                System.out.println("Счетчик id = " + manager.getId());

            } else if (numberMenu == 4) {
                manager.updateTask(updateTaskTest1);
                System.out.println("Счетчик id = " + manager.getId());

            } else if (numberMenu == 5) {
                manager.updateEpicTask(updateEpicTaskTest2);
                System.out.println("Счетчик id = " + manager.getId());

            } else if (numberMenu == 6) {
                manager.updateSubTask(updateSubTaskTest1);
                System.out.println("Счетчик id = " + manager.getId());

            } else if (numberMenu == 7) {
                manager.eraseStorageTask();

            } else if (numberMenu == 8) {
                manager.eraseStorageEpicTask();

            } else if (numberMenu == 9) {
                manager.eraseStorageSubTask();

            } else if (numberMenu == 10) {
                System.out.println(manager.getStorageTask());

            } else if (numberMenu == 11) {
                System.out.println(manager.getStorageEpicTask());

            } else if (numberMenu == 12) {
                System.out.println(manager.getStorageSubTask());

            } else if (numberMenu == 13) {
                System.out.println("Введите id Task для получения Task");
                System.out.println(manager.getTaskById(scanner.nextInt()));

            } else if (numberMenu == 14) {
                System.out.println("Введите id EpicTask для получения EpicTask");
                System.out.println(manager.getEpicTaskById(scanner.nextInt()));

            } else if (numberMenu == 15) {
                System.out.println("Введите id SubTask для получения SubTask");
                System.out.println(manager.getSubTaskById(scanner.nextInt()));

            } else if (numberMenu == 16) {
                System.out.println("Список задач Task =" + manager.getStorageTask().keySet());
                System.out.println("Список задач EpicTask =" + manager.getStorageEpicTask().keySet());
                System.out.println("Список задач SubTask =" + manager.getStorageSubTask().keySet());
                System.out.println("Введите id для удачения задачи");
                manager.deleteAnyTaskById(scanner.nextInt());
                System.out.println("Список задач Task =" + manager.getStorageTask().keySet());
                System.out.println("Список задач EpicTask =" + manager.getStorageEpicTask().keySet());
                System.out.println("Список задач SubTask =" + manager.getStorageSubTask().keySet());

            } else if (numberMenu == 17) {
                System.out.println("Введите id EpicTask для получения списка SubTask");
                System.out.println(manager.getSubTaskByEpicId(scanner.nextInt()));

            } else if (numberMenu == 0) {
                //Выход
                System.out.println("Пока!");
                scanner.close();
                return;

            } else {
                System.out.println("Такой команды нет");
            }
        }
    }
}