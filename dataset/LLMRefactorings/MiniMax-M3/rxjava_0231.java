public class rxjava_0231 {

    @Override
    public void onNext(T t) {
        if (done) {
            return;
        }
        
        R v;
        try {
            v = combineValues(t);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            dispose();
            onError(ex);
            return;
        }
        
        if (v == null) {
            return;
        }
        
        HalfSerializer.onNext(downstream, v, this, error);
    }
    
    private R combineValues(T t) {
        AtomicReferenceArray<Object> ara = values;
        int n = ara.length();
        Object[] objects = new Object[n + 1];
        objects[0] = t;
        
        for (int i = 0; i < n; i++) {
            Object o = ara.get(i);
            if (o == null) {
                return null;
            }
            objects[i + 1] = o;
        }
        
        return Objects.requireNonNull(combiner.apply(objects), "combiner returned a null value");
    }
}
