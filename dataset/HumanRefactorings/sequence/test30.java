public class test30 {
    private static final int INITIAL_COUNT = 3;

    static void foo(int i, boolean shouldPrint) {
        System.out.println(shouldPrint);
        if (i > 0) {
            foo(i - 1, shouldPrint);
        }
    }

    public static void main(String[] args) {
        foo(INITIAL_COUNT, true);
    }
}
