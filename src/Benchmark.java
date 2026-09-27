import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final long SEED = 42L;
    private static final int REPETITIONS = 5;

    public static void runAll() {
        new File("results/results.csv").mkdirs();

        try (PrintWriter w = new PrintWriter("results/results.csv")) {
            w.println("Structure,Workload,Operation,N,AvgTimeNs,MetricName,MetricValue");

            for (int n : SIZES) {
                runWorkload1(w, n);
                runWorkload2(w, n);
                runWorkload3(w, n);
                runWorkload4(w, n);
            }
            System.out.println("Benchmark saved to results/results.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runWorkload1(PrintWriter w, int n) {
        int[] indices = randomArray(10_000, n, SEED + 1);

        DynamicArray da = buildDA(n);
        double t1 = timeOp(() -> { da.resetMetrics(); for (int idx : indices) da.get(idx); });
        w.println(row("DynamicArray", "RandomAccess", "get", n, t1, "Accesses", da.getAccessCount()));

        LinkedList ll = buildLL(n);
        double t2 = timeOp(() -> { ll.resetMetrics(); for (int idx : indices) ll.get(idx); });
        w.println(row("LinkedList", "RandomAccess", "get", n, t2, "Accesses", ll.getAccessCount()));
    }

    private static void runWorkload2(PrintWriter w, int n) {
        int[] targets = randomArray(1_000, n * 10, SEED + 2);

        DynamicArray da = buildDA(n);
        double t1 = timeOp(() -> { da.resetMetrics(); for (int val : targets) da.contains(val); });
        w.println(row("DynamicArray", "Search", "contains", n, t1, "Comparisons", da.getComparisonCount()));

        LinkedList ll = buildLL(n);
        double t2 = timeOp(() -> { ll.resetMetrics(); for (int val : targets) ll.contains(val); });
        w.println(row("LinkedList", "Search", "contains", n, t2, "Comparisons", ll.getComparisonCount()));
    }

    private static void runWorkload3(PrintWriter w, int n) {
        runListInsertRemove(w, "DynamicArray", n);
        runListInsertRemove(w, "LinkedList", n);
    }

    private static void runListInsertRemove(PrintWriter w, String name, int n) {

        long totalInsertTime = 0;
        ListStructure lastAfterInsert = null;
        for (int rep = 0; rep < REPETITIONS; rep++) {
            ListStructure list = name.equals("DynamicArray") ? buildDA(n) : buildLL(n);
            list.resetMetrics();
            long start = System.nanoTime();
            for (int i = 0; i < 1_000; i++) list.add(0, i);
            totalInsertTime += System.nanoTime() - start;
            lastAfterInsert = list;
        }
        double tInsertBegin = totalInsertTime / (double) REPETITIONS;
        w.println(row(name, "InsertRemove", "Insert-Begin", n, tInsertBegin, "Movements", lastAfterInsert.getMovementCount()));

        long totalRemoveTime = 0;
        ListStructure lastAfterRemove = null;
        for (int rep = 0; rep < REPETITIONS; rep++) {
            ListStructure list = name.equals("DynamicArray") ? buildDA(n) : buildLL(n);
            list.resetMetrics();
            long start = System.nanoTime();
            for (int i = 0; i < 1_000 && list.size() > 0; i++) list.remove(0);
            totalRemoveTime += System.nanoTime() - start;
            lastAfterRemove = list;
        }
        double tRemoveBegin = totalRemoveTime / (double) REPETITIONS;
        w.println(row(name, "InsertRemove", "Remove-Begin", n, tRemoveBegin, "Movements", lastAfterRemove.getMovementCount()));
    }

    private static void runWorkload4(PrintWriter w, int n) {
        int[] values = randomArray(n, n * 10, SEED + 3);
        long totalInsertTime = 0;
        MinHeap lastAfterInsert = null;
        for (int rep = 0; rep < REPETITIONS; rep++) {
            MinHeap heap = new MinHeap();
            heap.resetMetrics();
            long start = System.nanoTime();
            for (int v : values) heap.insert(v);
            totalInsertTime += System.nanoTime() - start;
            lastAfterInsert = heap;
        }
        double tInsert = totalInsertTime / (double) REPETITIONS;
        w.println(row("MinHeap", "PriorityProcessing", "Insert", n, tInsert, "Comparisons", lastAfterInsert.getComparisonCount()));

        long totalExtractTime = 0;
        MinHeap lastAfterExtract = null;
        for (int rep = 0; rep < REPETITIONS; rep++) {
            MinHeap heap = new MinHeap();
            for (int v : values) heap.insert(v);
            heap.resetMetrics();
            long start = System.nanoTime();
            while (heap.size() > 0) heap.extractMin();
            totalExtractTime += System.nanoTime() - start;
            lastAfterExtract = heap;
        }
        double tExtract = totalExtractTime / (double) REPETITIONS;
        w.println(row("MinHeap", "PriorityProcessing", "Extract", n, tExtract, "Comparisons", lastAfterExtract.getComparisonCount()));
    }

    private static double timeOp(Runnable task) {
        long totalTime = 0;
        for (int i = 0; i < REPETITIONS; i++) {
            long start = System.nanoTime();
            task.run();
            totalTime += System.nanoTime() - start;
        }
        return totalTime / (double) REPETITIONS;
    }

    private static DynamicArray buildDA(int n) {
        DynamicArray da = new DynamicArray();
        int[] vals = randomArray(n, n * 10, SEED);
        for (int v : vals) da.add(v);
        return da;
    }

    private static LinkedList buildLL(int n) {
        LinkedList ll = new LinkedList();
        int[] vals = randomArray(n, n * 10, SEED);
        for (int v : vals) ll.add(v);
        return ll;
    }

    private static int[] randomArray(int count, int bound, long seed) {
        Random r = new Random(seed);
        int[] arr = new int[count];
        for (int i = 0; i < count; i++) arr[i] = r.nextInt(Math.max(1, bound));
        return arr;
    }

    private static String row(String struct, String wl, String op, int n, double time, String metric, long metricVal) {
        return struct + "," + wl + "," + op + "," + n + "," + time + "," + metric + "," + metricVal;
    }
}