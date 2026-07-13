class ParentClass {
    void f() {
        // extract method, new method name 'method'
        final int i;
        System.out.println("parent class");
    }
}

class SourceClass {
    void method() {
        printSourceClass();
    }

    private void printSourceClass() {
        System.out.println("source class");
    }

    class InnerClass extends ParentClass {
        void testMethod() {
            method();
        }
    }
}
