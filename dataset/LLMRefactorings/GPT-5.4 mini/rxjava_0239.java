public class rxjava_0239 {

            @Override
            public boolean tryOnNext_mini_0239(T t) {
                if (done) {
                    return false;
                }
                long retries = 0;
    
                for (;;) {
                    try {
                        onNext.accept(t);
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
    
                    downstream.onNext(t);
                    return true;
                }
            }
}
