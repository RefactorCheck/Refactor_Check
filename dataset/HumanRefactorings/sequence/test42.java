public class test42 {

    public int x;

    public class Inner {
        public int x;

        public void foo() {
            final int outerX = test42.this.x;
            x = outerX + 1;
        }
    }
}
