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

    @Override public int remove(int index) { return 0; }
    @Override public int get(int index) { return 0; }
    @Override public boolean contains(int x) { return false; }
    @Override public void resetMetrics() {}
    @Override public long getAccessCount() { return 0; }
    @Override public long getComparisonCount() { return 0; }
    @Override public long getMovementCount() { return 0; }
}