public class test37 {
    public static void main(String[] args) {
        final int arraySize = 3;
        boolean values[] = new boolean[arraySize];
        Arrays.fill(values, true);

        for (boolean value : values) {
            System.out.println("Value = " + value);
        }
    }
}
