package Manager;

import Tasks.Task;

final class Node {
    private final Task task;
    private Node prev = null;
    private Node next = null;

    public Node(Task task) {
        this.task = task;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public Node getNext() {
        return next;
    }

    public Task getTask() {
        return task;
    }
}
