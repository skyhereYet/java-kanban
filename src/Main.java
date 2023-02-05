import Manager.Manager;

public class Main {
    public static void main(String[] args) {
        //Формирование теста TestClass
        TestClass testClass = new TestClass();
        testClass.setHashMap1();
        testClass.setHashMap2();
        //конец формирования теста
        Manager manager = new Manager();
        testClass.startTest(manager);
    }
}
