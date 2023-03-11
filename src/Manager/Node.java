package Manager;

import Tasks.Task;

final class Node {
    private final Task taskId;
    private Node prev = null;
    private Node next = null;

    public Node(Task taskId) {
        this.taskId = taskId;
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

    public Task getTaskId() {
        return taskId;
    }
}
