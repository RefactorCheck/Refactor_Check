package testProject;
public class Main {
    static String[]field;
    public static void main(String[]inputArgs) {
        int x = (field = inputArgs).length;
        int y = field.hashCode();
        int total = y + x;
    }
}
