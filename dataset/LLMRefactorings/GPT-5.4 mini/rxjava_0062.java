public class rxjava_0062 {

            @Override
            public boolean tryOnNext(T t, Object unused_0062) {
                if (!done) {
                    boolean b;
    
                    try {
                        b = predicate.test(t);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        cancel();
                        onError(ex);
                        return false;
                    }
    
                    if (b) {
                        return downstream.tryOnNext(t);
                    }
                }
                return false;
            }
}
