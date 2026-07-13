public class rxjava_0070 {

        @Override
        protected void subscribeActual(Subscriber<? super T> subscriber) {
            CancellableQueueFuseable<T> qs = new CancellableQueueFuseable<>();
            subscriber.onSubscribe(qs);
    
            if (!qs.isDisposed()) {
    
                try {
                    action.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    if (!qs.isDisposed()) {
                        subscriber.onError(ex);
                    } else {
                        RxJavaPlugins.onError(ex);
                    }
                    return;
                }
    
                if (!qs.isDisposed()) {
                    subscriber.onComplete();
                }
            }
        }
}
