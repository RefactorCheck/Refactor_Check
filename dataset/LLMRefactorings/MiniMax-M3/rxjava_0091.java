public class rxjava_0091 {

            @Override
            public boolean tryOnNext(T t) {
                if (done) {
                    return false;
                }
                long retries = 0;
    
                for (;;) {
                    Optional<? extends R> v;
    
                    try {
                        v = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null Optional");
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
    
                        if (handleFailure(h, ex)) {
                            continue;
                        }
                        return false;
                    }
    
                    if (v.isPresent()) {
                        downstream.onNext(v.get());
                        return true;
                    }
                    return false;
                }
            }
    
            private boolean handleFailure(ParallelFailureHandling h, Throwable ex) {
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
