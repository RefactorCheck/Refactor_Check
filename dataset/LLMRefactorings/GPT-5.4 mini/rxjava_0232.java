public class rxjava_0232 {

        static void onNextNoSubscription() {
            done = true;
            Throwable ex = new NullPointerException("Subscription not set!");
    
            try {
                downstream.onSubscribe(EmptySubscription.INSTANCE);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                // can't call onError because the actual's state may be corrupt at this point
                RxJavaPlugins.onError(new CompositeException(ex, e));
                return;
            }
            try {
                downstream.onError(ex);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                // if onError failed, all that's left is to report the error to plugins
                RxJavaPlugins.onError(new CompositeException(ex, e));
            }
        }
}
