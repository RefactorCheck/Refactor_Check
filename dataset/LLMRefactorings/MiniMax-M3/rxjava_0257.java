public class rxjava_0257 {

    void onCompleteNoSubscription() {
        Throwable ex = new NullPointerException("Subscription not set!");

        try {
            downstream.onSubscribe(EmptyDisposable.INSTANCE);
        } catch (Throwable e) {
            reportFatalError(ex, e);
            return;
        }
        try {
            downstream.onError(ex);
        } catch (Throwable e) {
            reportFatalError(ex, e);
        }
    }

    private void reportFatalError(Throwable original, Throwable error) {
        Exceptions.throwIfFatal(error);
        RxJavaPlugins.onError(new CompositeException(original, error));
    }
}
