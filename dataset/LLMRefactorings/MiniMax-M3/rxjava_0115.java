public class rxjava_0115 {

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
                Exceptions.throwIfFatal(ex);

                if (handleError(ex, retries)) {
                    retries++;
                    continue;
                }
                return false;
            }

            downstream.onNext(v);
            return true;
        }
    }

    private boolean handleError(Throwable ex, long retries) {
        ParallelFailureHandling h;

        try {
            h = Objects.requireNonNull(errorHandler.apply(retries + 1, ex), "The errorHandler returned a null ParallelFailureHandling");
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
