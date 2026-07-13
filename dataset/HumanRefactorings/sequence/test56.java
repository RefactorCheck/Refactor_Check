class test56 {
    static void foo1(Number n) {
        System.out.println("1");
    }
    //change method signature: new method name "foo1"
    static void foo1(Long i) {
        System.out.println("2");
    }
    public static void main(String[] args) {
        long value = 0;
        foo1((Number)value);// look here
    }
}
