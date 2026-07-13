public class test21 extends JComponent {

    private void f() {
        extracted();
    }

    private static void extracted() {
        Color background = Color.RED;
        setBackground(background);
    }

    private class B extends JComponent {
        void g() {
            Color c = Color.RED;
            setBackground(c);
        }
    }
}
