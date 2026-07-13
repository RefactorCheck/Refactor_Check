public class test39 {
    public static void main(String[] args) {
        FileFilter javaFilter = file -> file.getName().endsWith(".java");
        javaFilter.getClass();
    }
}
