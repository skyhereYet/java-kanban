import Manager.Managers;
import Manager.TaskManager;

public class Main {
    public static void main(String[] args) {
        //Формирование теста TestClass
        TestClass testClass = new TestClass();
        testClass.setHashMap1();
        testClass.setHashMap2();
        //конец формирования теста

        TaskManager taskManager = Managers.getDefault();
        testClass.startTest(taskManager);
    }
}
