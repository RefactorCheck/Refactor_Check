public class rxjava_0188 {

    @Override
    public void onError(Throwable t) {
        long r = remaining;
        if (r != Long.MAX_VALUE) {
            remaining = r - 1;
        }
        if (r == 0) {
            downstream.onError(t);
        } else if (handleErrorOrRetry(t)) {
            subscribeNext();
        }
    }

    private boolean handleErrorOrRetry(Throwable t) {
        boolean b;
        try {
            b = predicate.test(t);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            downstream.onError(new CompositeException(t, e));
            return false;
        }
        if (!b) {
            downstream.onError(t);
            return false;
        }
        return true;
    }
}
