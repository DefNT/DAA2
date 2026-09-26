public class LinkedList implements ListStructure {

    private static class Node {
        int value;
        Node next;
        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    private long accessCount = 0;
    private long comparisonCount = 0;
    private long movementCount = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int x) {
        Node node = new Node(x);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
        movementCount++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == size) {
            add(x);
            return;
        }

        Node newNode = new Node(x);
        if (index == 0) {
            newNode.next = head;
            head = newNode;
            if (size == 0) tail = newNode;
        } else {
            Node prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
                movementCount++;
            }
            newNode.next = prev.next;
            prev.next = newNode;
        }
        size++;
        movementCount++;
    }

    @Override
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        int removedValue;
        if (index == 0) {
            removedValue = head.value;
            head = head.next;
            if (head == null) tail = null;
        } else {
            Node prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
                movementCount++;
            }
            Node toRemove = prev.next;
            removedValue = toRemove.value;
            prev.next = toRemove.next;
            if (toRemove == tail) tail = prev;
        }
        size--;
        movementCount++;
        return removedValue;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
            accessCount++;
        }
        accessCount++;
        return current.value;
    }

    @Override
    public boolean contains(int x) {
        Node current = head;
        while (current != null) {
            comparisonCount++;
            if (current.value == x) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void resetMetrics() {
        accessCount = 0;
        comparisonCount = 0;
        movementCount = 0;
    }

    @Override
    public long getAccessCount() {
        return accessCount;
    }

    @Override
    public long getComparisonCount() {
        return comparisonCount;
    }

    @Override
    public long getMovementCount() {
        return movementCount;
    }
}