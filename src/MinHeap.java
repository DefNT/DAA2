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

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            comparisonCount++;
            if (data[index] < data[parent]) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size) {
                comparisonCount++;
                if (data[left] < data[smallest]) {
                    smallest = left;
                }
            }
            if (right < size) {
                comparisonCount++;
                if (data[right] < data[smallest]) {
                    smallest = right;
                }
            }

            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        int min = data[0];
        size--;
        data[0] = data[size];

        if (size > 0) {
            siftDown(0);
        }

        return min;
    }
}