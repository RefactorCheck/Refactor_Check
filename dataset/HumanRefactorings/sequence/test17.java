public class test17 {
    String[] Lines;
    int i = 0;
    private static void increment() {
        i++;
    }
    public static void main(String[] args) {
        final String currentLine = Lines[i];
        log.warn(currentLine);
       log.warn(currentLine);
       increment();
       log.warn(lines[i]);
    }
}
