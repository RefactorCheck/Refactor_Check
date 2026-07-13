class A extends C {
}

class B {
    private static final int PRINT_VALUE = 2;

    void main() {
        final int capturedValue = PRINT_VALUE;
        new A() {
            void m() {
                System.out.println(capturedValue);
            }
        };
    }
}
class C {
    protected final int b = 1;
}
