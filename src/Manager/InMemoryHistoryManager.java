package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    //Хранилище истории просмотров
    private List<Task> storageHistory = new ArrayList<>();
    private final CustomLinkedList historyLinkedList = new CustomLinkedList();

    @Override
    public List<Task> getHistory() {
        storageHistory = historyLinkedList.getTasks();
        return storageHistory;
    }

    @Override
    public void add(Task task) {
        historyLinkedList.linkLast(task);
    }

    @Override
    public void remove(Task task) {
        historyLinkedList.removeNode(task);
    }
}

final class CustomLinkedList {
    private final Node head;
    private final Node tail;
    private final Map<Integer, Node> mapHistory = new HashMap<>();

    public CustomLinkedList() {
        this.head = new Node(null);
        this.tail = new Node(null);
        head.setNext(tail);
        tail.setPrev(head);
    }

    //добавить в конец списка Node
    public void linkLast(Task task) {
        if (mapHistory.containsKey(task.getId())) {
            removeNode(mapHistory.get(task.getId()).getTask());
            mapHistory.remove(task.getId());
        }
        Node newNode = new Node(task);
        newNode.setPrev(tail.getPrev());
        newNode.setNext(tail);
        tail.getPrev().setNext(newNode);
        tail.setPrev(newNode);
        mapHistory.put(task.getId(), newNode);
    }

    //удалить Node
    public void removeNode(Task task) {
        if (mapHistory.containsKey(task.getId())) {
            Node nodeToRemove = mapHistory.get(task.getId());
            nodeToRemove.getPrev().setNext(nodeToRemove.getNext());
            nodeToRemove.getNext().setPrev(nodeToRemove.getPrev());
            nodeToRemove.setNext(null);
            nodeToRemove.setPrev(null);
        }
    }

    //собрать список всех задач в ArrayList
    public List<Task> getTasks() {
        List<Task> listToReturn = new ArrayList<>();
        Node current = head.getNext();
        while (current != tail) {
            listToReturn.add(current.getTask());
            current = current.getNext();
        }
        return listToReturn;
    }


}
