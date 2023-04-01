package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    //вывести историю просмотров задач
    List<Task> getHistory();

    //заполнить историю просмотров задач
    void add(Task task);

    //удалить задачу из истории просмотров
    void remove(Task task);

    //создание строки из списка истории
    static String historyToString(HistoryManager historyManager) {
        StringBuffer toReturn = new StringBuffer();
        for (int numHistory = 0; numHistory < historyManager.getHistory().size(); numHistory++) {
            toReturn.append(historyManager.getHistory().get(numHistory).getId());
            if (numHistory < historyManager.getHistory().size()-1) {
                toReturn.append(",");
            }
        }
        return toReturn.toString();
    }

    //создание списка истории из строки
    static List<Integer> historyFromString(String line) {
        String[] valuesHistory = line.split(",");
        List<Integer> listToReturn = new ArrayList<>();
        for (String subString : valuesHistory) {
            listToReturn.add(Integer.parseInt(subString));
        }
        return listToReturn;
    }
}
