public class test2 {
    public static void main(String[] args) {
        final int baseCase = 0;
        Function<Integer, Integer> fact = value -> value == baseCase? 1 : value * fact.apply(value-1);
    }
}
