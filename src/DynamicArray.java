public class DynamicArray implements ListStructure {

    private int[] data;
    private int size;

    private long accessCount = 0;
    private long comparisonCount = 0;
    private long movementCount = 0;

    private static final int DEFAULT_CAPACITY = 10;

    public DynamicArray() {
        data = new int[DEFAULT_CAPACITY];
        size = 0;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            int[] newData = new int[data.length * 2];
            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
    }

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