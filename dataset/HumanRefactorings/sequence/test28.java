public class test28 {
    public static void main(String[] args) {
        int value = 10;
        if (args.length == 0) {
            value = 0;
        }
        printResult(args, value);
    }

    private static void printResult(String[] args, int x) {
        if (x > 0) {
            System.out.println(args[0]);
        } else {
            System.out.println(0);
        }
    }
}
