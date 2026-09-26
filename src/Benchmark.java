import java.io.File;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final long SEED = 42L;
    private static final int REPETITIONS = 5;

    public static void runAll() {
        new File("results/results.csv").mkdirs();
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