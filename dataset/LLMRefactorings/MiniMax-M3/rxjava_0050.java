public class rxjava_0050 {

    @Override
    public void onNext(T t) {
        if (done) {
            return;
        }
        try {
            if (predicate.test(t)) {
                done = true;
                upstream.cancel();
                complete(true);
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            upstream.cancel();
            onError(e);
        }
    }
}
