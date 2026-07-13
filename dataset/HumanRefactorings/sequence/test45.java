class A {
}
class B {
    void main() {
        final int localB = 2;
        new A() {
            void m() {
                System.out.println(localB);
            }
        };
    }
}
class C {
    A a;
    protected final int b = 1;
}
