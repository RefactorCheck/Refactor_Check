public class rxjava_0031 {

    @Override
    public void onNext(T t) {
        if (done) {
            return;
        }
        if (!testPredicate(t)) {
            return;
        }
        downstream.onNext(t);
    }

    private boolean testPredicate(T t) {
        try {
            return predicate.test(t);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            upstream.cancel();
            onError(e);
            return false;
        }
    }
}
