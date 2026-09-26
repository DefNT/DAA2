public interface ListStructure {
    void add(int x);
    void add(int index, int x);
    int remove(int index);
    int get(int index);
    boolean contains(int x);
    int size();

    void resetMetrics();
    long getAccessCount();
    long getComparisonCount();
    long getMovementCount();
}