public class Tests {

    public static void runAll() {
        testDynamicArray();
        testLinkedList();
        System.out.println("Tests passed");
    }

    private static void testDynamicArray() {
        DynamicArray list = new DynamicArray();

        check(list.size() == 0, "DA: empty size");
        check(!list.contains(5), "DA: empty contains");

        list.add(10);
        list.add(20);
        check(list.get(0) == 10 && list.get(1) == 20, "DA: basic get");
        check(list.contains(20) && !list.contains(99), "DA: contains check");

        list.add(1, 15);
        check(list.get(1) == 15, "DA: add at index");
        check(list.remove(1) == 15, "DA: remove at index");

        try {
            list.get(10);
            check(false, "DA: get out of bounds");
        } catch (IndexOutOfBoundsException e) {}
        try {
            list.remove(-1);
            check(false, "DA: remove out of bounds");
        } catch (IndexOutOfBoundsException e) {}
    }

    private static void testLinkedList() {
        LinkedList list = new LinkedList();

        check(list.size() == 0, "LL: empty size");

        list.add(10);
        list.add(20);
        check(list.get(0) == 10 && list.get(1) == 20, "LL: basic get");
        check(list.contains(20) && !list.contains(99), "LL: contains check");

        list.add(0, 5);
        list.add(2, 15);
        check(list.get(0) == 5 && list.get(2) == 15, "LL: add middle/head");
        check(list.remove(0) == 5, "LL: remove head");

        try {
            list.get(100);
            check(false, "LL: get out of bounds");
        } catch (IndexOutOfBoundsException e) {}
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException("Test Failed: " + message);
        }
    }
}