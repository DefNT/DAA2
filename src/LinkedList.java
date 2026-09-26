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

    @Override public void add(int x) {}
    @Override public void add(int index, int x) {}
    @Override public int remove(int index) { return 0; }
    @Override public int get(int index) { return 0; }
    @Override public boolean contains(int x) { return false; }
    @Override public void resetMetrics() {}
    @Override public long getAccessCount() { return 0; }
    @Override public long getComparisonCount() { return 0; }
    @Override public long getMovementCount() { return 0; }
}