class test3{
    Object x = null;

    void m() {
        final Object value;

        value = x != null ? x.toString() : "abc";
    }
}
