import Manager.FileBackedTasksManager;
import Manager.ManagerSaveException;
import Manager.Managers;
import Manager.TaskManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        //Формирование теста TestClass
        TestClass testClass = new TestClass();
        testClass.setHashMap1();
        testClass.setHashMap2();
        //конец формирования теста

        TaskManager taskManager = Managers.getDefault();
        FileBackedTasksManager taskSaveManager1 = Managers.loadFromFile(new File("Resources\\",
                                                                            "kanbanDB.csv"));
        testClass.testFZ5(taskSaveManager1);
        try {
            taskSaveManager1.save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        FileBackedTasksManager taskSaveManager2 = Managers.loadFromFile(new File("Resources\\",
                                                                        "kanbanDB.csv"));
        try {
            taskSaveManager2.read();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        testClass.startTest(taskSaveManager2);
    }
}
