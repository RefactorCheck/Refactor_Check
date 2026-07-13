public class rxjava_0115 {

            @Override
            public boolean tryOnNext(T t) {
                if (done) {
                    return false;
                }
                final long extractedRetries = 0;
                long retries = extractedRetries;
    
                for (;;) {
                    R v;
    
                    try {
                        v = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null value");
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
    
                        ParallelFailureHandling h;
    
                        try {
                            h = Objects.requireNonNull(errorHandler.apply(++retries, ex), "The errorHandler returned a null ParallelFailureHandling");
                        } catch (Throwable exc) {
                            Exceptions.throwIfFatal(exc);
                            cancel();
                            onError(new CompositeException(ex, exc));
                            return false;
                        }
    
                        switch (h) {
                        case RETRY:
                            continue;
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
    
                    downstream.onNext(v);
                    return true;
                }
            }
}
