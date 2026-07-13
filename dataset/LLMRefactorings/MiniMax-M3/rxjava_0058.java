public class rxjava_0058 {

    @Override
    public boolean tryOnNext(T t) {
        if (done) {
            return false;
        }
        long retries = 0;

        for (;;) {
            R v;

            try {
                v = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null value");
            } catch (Throwable ex) {
                retries++;
                if (handleError(ex, retries)) {
                    continue;
                }
                return false;
            }

            return downstream.tryOnNext(v);
        }
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
