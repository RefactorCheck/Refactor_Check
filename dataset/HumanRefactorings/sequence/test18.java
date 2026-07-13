public class test18 {
    private static final String SEPARATOR = ": ";
    String[] lines;
    int i = 0;
    private String foo(String message) {
        return message + SEPARATOR + lines[i++];
    }

    public static void main(String[] args) {
        final String test = foo("test");
        log.warn(test);
        log.warn(foo("test"));
    }
}
