public class rxjava_0232 {

    void onNextNoSubscription() {
        done = true;
        Throwable ex = new NullPointerException("Subscription not set!");

        try {
            downstream.onSubscribe(EmptySubscription.INSTANCE);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            reportError(ex, e);
            return;
        }
        try {
            downstream.onError(ex);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            reportError(ex, e);
        }
    }

    private void reportError(Throwable ex, Throwable e) {
        RxJavaPlugins.onError(new CompositeException(ex, e));
    }
}
