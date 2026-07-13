public class rxjava_0136 {

    private static final String MAPPER_NULL_MESSAGE = "The mapper function returned a null value.";

            @Override
            public boolean tryOnNext(T t) {
                if (done) {
                    return true;
                }
    
                if (sourceMode != NONE) {
                    downstream.tryOnNext(null);
                    return true;
                }
    
                U v;
    
                try {
                    v = Objects.requireNonNull(mapper.apply(t), MAPPER_NULL_MESSAGE);
                } catch (Throwable ex) {
                    fail(ex);
                    return true;
                }
                return downstream.tryOnNext(v);
            }
}
