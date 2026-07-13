public class rxjava_0242 {

            @Override
            public boolean tryOnNext(T t) {
                if (!done) {
                    long retries = 0L;
    
                    for (;;) {
                        try {
                            return predicate.test(t) && downstream.tryOnNext(t);
                        } catch (Throwable ex) {
                            if (!handleError(ex, ++retries)) {
                                return false;
                            }
                        }
                    }
                }
                return false;
            }
    
            private boolean handleError(Throwable ex, long retries) {
                Exceptions.throwIfFatal(ex);
    
                ParallelFailureHandling h;
    
                try {
                    h = Objects.requireNonNull(errorHandler.apply(retries, ex), "The errorHandler returned a null ParallelFailureHandling");
                } catch (Throwable exc) {
                    Exceptions.throwIfFatal(exc);
                    cancel();
                    onError(new CompositeException(ex, exc));
                    return false;
                }
    
                switch (h) {
                case RETRY:
                    return true;
                case SKIP:
                    return false;
                case STOP:
                    cancel();
                    onComplete();
                    return false;
                default:
                    cancel();
                    onError(ex);
                    return false;
                }
            }
}
