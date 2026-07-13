public class rxjava_0035 {

            @Override
            public boolean tryOnNext(T t) {
                if (!done) {
                    long retries = 0L;
    
                    for (;;) {
                        boolean b;
    
                        try {
                            b = predicate.test(t);
                        } catch (Throwable ex) {
                            Long next = handleError(ex, retries);
                            if (next == null) {
                                return false;
                            }
                            retries = next;
                            continue;
                        }
    
                        if (b) {
                            downstream.onNext(t);
                            return true;
                        }
                        return false;
                    }
                }
                return false;
            }
    
            private Long handleError(Throwable ex, long currentRetries) {
                Exceptions.throwIfFatal(ex);
    
                ParallelFailureHandling h;
    
                try {
                    h = Objects.requireNonNull(errorHandler.apply(++currentRetries, ex), "The errorHandler returned a null ParallelFailureHandling");
                } catch (Throwable exc) {
                    Exceptions.throwIfFatal(exc);
                    cancel();
                    onError(new CompositeException(ex, exc));
                    return null;
                }
    
                switch (h) {
                case RETRY:
                    return currentRetries;
                case SKIP:
                    return null;
                case STOP:
                    cancel();
                    onComplete();
                    return null;
                default:
                    cancel();
                    onError(ex);
                    return null;
                }
            }
}
