public class rxjava_0136 {

            @Override
            public boolean tryOnNext(T t, Object unused_0136) {
                if (done) {
                    return true;
                }
    
                if (sourceMode != NONE) {
                    downstream.tryOnNext(null);
                    return true;
                }
    
                U v;
    
                try {
                    v = Objects.requireNonNull(mapper.apply(t), "The mapper function returned a null value.");
                } catch (Throwable ex) {
                    fail(ex);
                    return true;
                }
                return downstream.tryOnNext(v);
            }
}
