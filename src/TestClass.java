import Manager.FileBackedTasksManager;
import Manager.ManagerSaveException;
import Manager.TaskManager;
import Tasks.*;
import java.util.HashMap;
import java.util.Scanner;

class TestClass {
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
    SubTask updateSubTaskTest1 = new SubTask();
    //создаем списки подзадача
    HashMap<Integer, SubTask> testStorageSubtask1 = new HashMap<>();
    HashMap<Integer, SubTask> testStorageSubtask2 = new HashMap<>();
    //Epic задачи
    EpicTask epicTaskTest1 = new EpicTask("First EpicTask", "First epictask for example",
            TaskStatus.NEW);
    EpicTask epicTaskTest2 = new EpicTask("Second EpicTask", "Second epictask for example",
            TaskStatus.IN_PROGRESS);
    EpicTask updateEpicTaskTest2 = new EpicTask("Updated EpicTask", "Updated epictask for example",
            TaskStatus.IN_PROGRESS);

    protected void setHashMap1(){
        testStorageSubtask1.put(subTaskTest1.getId(), subTaskTest1);
        testStorageSubtask1.put(subTaskTest3.getId(), subTaskTest3);
    }

    protected void setHashMap2(){
        testStorageSubtask2.put(subTaskTest2.getId(), subTaskTest2);
    }

    public void printMenu() {
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
        System.out.println("18 - Вывести историю просмотров");
        System.out.println("19 - Экспресс тест по ФЗ5");
        System.out.println("20 - Сохранить данные");
        System.out.println("21 - Прочитать данные");
        System.out.println("0 - Выйти из приложения");
    }

    public void startTest(TaskManager taskManager) {
        while (true) {
            printMenu(); //вывод меню
            int numberMenu = scanner.nextInt();

            if (numberMenu == 1) {
                System.out.println(taskManager.getStorageTask());
                taskManager.createTask(taskTest1);
                taskManager.createTask(taskTest2);
                taskManager.createTask(taskTest3);
                System.out.println("Задачи Task добавлены" + taskManager.getStorageTask());
                System.out.println("Счетчик id = " + taskManager.getId());

            } else if (numberMenu == 2) {
                System.out.println(taskManager.getStorageEpicTask());
                taskManager.createEpicTask(epicTaskTest1);
                taskManager.createEpicTask(epicTaskTest2);
                System.out.println("Задачи EpicTask добавлены" + taskManager.getStorageEpicTask());
                System.out.println("Счетчик id = " + taskManager.getId());

            } else if (numberMenu == 3) {
                System.out.println(taskManager.getStorageSubTask());
                taskManager.createSubTask(subTaskTest4);
                System.out.println("Задачи SubTask добавлены" + taskManager.getStorageSubTask());
                System.out.println("Счетчик id = " + taskManager.getId());

            } else if (numberMenu == 4) {
                taskManager.updateTask(updateTaskTest1);
                System.out.println("Счетчик id = " + taskManager.getId());

            } else if (numberMenu == 5) {
                System.out.println("Введите обновляемый Epic id");
                updateEpicTaskTest2.setId(scanner.nextInt());
                taskManager.updateEpicTask(updateEpicTaskTest2);
                System.out.println("Счетчик id = " + taskManager.getId());

            } else if (numberMenu == 6) {
                System.out.println("Введите обновляемый SubTask id");
                updateSubTaskTest1.setId(scanner.nextInt());
                System.out.println("Введите обновляемый EpicTask id");
                updateSubTaskTest1.setIdEpicTask(scanner.nextInt());
                updateSubTaskTest1.setDescription("Updated subtask for example");
                updateSubTaskTest1.setName("Updated SubTask");
                System.out.println("Введите статус SubTask (1 - NEW, 2 - IN_PROGRESS, 3 - DONE)");
                switch (scanner.nextInt()) {
                    case 1:
                        updateSubTaskTest1.setStatus(TaskStatus.NEW);
                        break;
                    case 2:
                        updateSubTaskTest1.setStatus(TaskStatus.IN_PROGRESS);
                        break;
                    case 3:
                        updateSubTaskTest1.setStatus(TaskStatus.DONE);
                        break;
                }
                taskManager.updateSubTask(updateSubTaskTest1);
                System.out.println("Счетчик id = " + taskManager.getId());

            } else if (numberMenu == 7) {
                taskManager.eraseStorageTask();

            } else if (numberMenu == 8) {
                taskManager.eraseStorageEpicTask();

            } else if (numberMenu == 9) {
                taskManager.eraseStorageSubTask();

            } else if (numberMenu == 10) {
                for (Task task : taskManager.getStorageTask()) {
                    System.out.println(task);
                }

            } else if (numberMenu == 11) {
                for (Task task : taskManager.getStorageEpicTask()) {
                    System.out.println(task);
                }

            } else if (numberMenu == 12) {
                for (Task task : taskManager.getStorageSubTask()) {
                    System.out.println(task);
                }

            } else if (numberMenu == 13) {
                System.out.println("Введите id Task для получения Task");
                System.out.println(taskManager.getTaskById(scanner.nextInt()));
                getHistory(taskManager);

            } else if (numberMenu == 14) {
                System.out.println("Введите id EpicTask для получения EpicTask");
                System.out.println(taskManager.getEpicTaskById(scanner.nextInt()));
                getHistory(taskManager);

            } else if (numberMenu == 15) {
                System.out.println("Введите id SubTask для получения SubTask");
                System.out.println(taskManager.getSubTaskById(scanner.nextInt()));
                getHistory(taskManager);

            } else if (numberMenu == 16) {
                System.out.println("Список задач Task =" + taskManager.getStorageTask());
                System.out.println("Список задач EpicTask =" + taskManager.getStorageEpicTask());
                System.out.println("Список задач SubTask =" + taskManager.getStorageSubTask());
                System.out.println("Введите id для удачения задачи");
                taskManager.deleteAnyTaskById(scanner.nextInt());
                System.out.println("Список задач Task =" + taskManager.getStorageTask());
                System.out.println("Список задач EpicTask =" + taskManager.getStorageEpicTask());
                System.out.println("Список задач SubTask =" + taskManager.getStorageSubTask());

            } else if (numberMenu == 17) {
                System.out.println("Введите id EpicTask для получения списка SubTask");
                System.out.println(taskManager.getSubTaskByEpicId(scanner.nextInt()));

            } else if (numberMenu == 18) {
                System.out.println("Показать историю просмотра задач");
                getHistory(taskManager);

            } else if (numberMenu == 19) {
                System.out.println("Экспресс тест по ФЗ5");
                testFZ5(taskManager);

            } else if (numberMenu == 20) {
                System.out.println("Сохранить!");
                FileBackedTasksManager taskSaveManager = (FileBackedTasksManager) taskManager;
                try {
                    taskSaveManager.save();
                } catch (ManagerSaveException e) {
                    System.out.println(e.getMessage());
                }

            } else if (numberMenu == 21) {
                System.out.println("Читать!");
                FileBackedTasksManager taskSaveManager = (FileBackedTasksManager) taskManager;
                try {
                    taskSaveManager.read();
                } catch (ManagerSaveException e) {
                    System.out.println(e.getMessage());
                }


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

    public void testFZ5(TaskManager taskManager) {
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
        getHistory(taskManager);
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
        getHistory(taskManager);
        /*//удалить Task
        System.out.println("Удаляем Task id=2");
        taskManager.deleteAnyTaskById(2);
        //вывести историю
        getHistory(taskManager);
        //удалить Epictask с тремя подзадачами
        System.out.println("Удаляем Эпик id=3 с подзадачами: 4, 5, 6");
        taskManager.deleteAnyTaskById(3);
        //вывести историю
        getHistory(taskManager);*/
    }

    public void getHistory(TaskManager taskManager){
        System.out.print("История просмотра: ");
        for (Task task : taskManager.getHistory()){
            System.out.print(task.getId() + ", ");
        }
        System.out.println("\n");
    }
}
