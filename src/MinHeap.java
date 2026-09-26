public class MinHeap {

    private int[] data;
    private int size;
    private long comparisonCount = 0;

    private static final int DEFAULT_CAPACITY = 10;

    public MinHeap() {
        data = new int[DEFAULT_CAPACITY];
        size = 0;
    }

    public void resetMetrics() {
        comparisonCount = 0;
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    public int size() {
        return size;
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

    public void insert(int x) {
        ensureCapacity();
        data[size] = x;
        size++;
        siftUp(size - 1);
    }

    private void siftUp(int index) {}
}