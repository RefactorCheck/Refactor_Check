public class rxjava_0084 {

    @Override
    public void onNext(T t) {
        if (done) {
            return;
        }
        boolean b;
        try {
            b = predicate.test(t);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            cancelUpstream();
            onError(e);
            return;
        }
        if (b) {
            done = true;
            cancelUpstream();
            downstream.onSuccess(true);
        }
    }

    private void cancelUpstream() {
        upstream.cancel();
        upstream = SubscriptionHelper.CANCELLED;
    }
}
