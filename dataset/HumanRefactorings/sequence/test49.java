class A {
    void m(Object m) {
        System.out.println("A");
    }
}

class B extends A {
    void k() {
        String text=null;
        System.out.println(text);
    }

    void f() {
        m("1");
    }
}
