class test13 {
    class Model<Row extends java.util.List> {
        Row getObjectAtRow(){
            return new java.util.HashSet<Row>().iterator().next();
        }
    }
    class UI<Clazz extends java.util.ArrayList>{
    }
    Model<java.util.ArrayList> tableModel_;
    void foo(){
        new UI<java.util.ArrayList>();
        Class<?> rowClass = tableModel_.getObjectAtRow().getClass();
        java.lang.reflect.Array.newInstance
                (rowClass, 1);
    }
}
