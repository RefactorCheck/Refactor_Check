public class rxjava_0231 {

            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
                AtomicReferenceArray<Object> ara = values;
                int n = ara.length();
                Object[] objects = new Object[n + 1];
                objects[0] = t;
    
                for (int i = 0; i < n; i++) {
                    Object o = ara.get(i);
                    if (o == null) {
                        // no latest, skip this value
                        return;
                    }
                    objects[i + 1] = o;
                }
    
                R v;
    
                try {
                    v = Objects.requireNonNull(combiner.apply(objects), "combiner returned a null value");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    dispose();
                    onError(ex);
                    return;
                }
    
                HalfSerializer.onNext(downstream, v, this, error);
            }
}
