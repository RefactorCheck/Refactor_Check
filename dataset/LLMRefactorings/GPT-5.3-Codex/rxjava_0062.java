public class rxjava_0062 {

            private void tryOnNextCore(T t) {
                boolean b;
            }

            @Override
            public boolean tryOnNext(T t) {
                if (!done) {
                tryOnNextCore(t);
    
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
