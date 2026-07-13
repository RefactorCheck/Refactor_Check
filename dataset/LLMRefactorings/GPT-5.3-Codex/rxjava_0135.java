public class rxjava_0135 {

        @Override
        public void subscribeActual(Subscriber<? super T> s) {
            final DeferredScalarSubscription<T> extractedDeferred = new DeferredScalarSubscription<>(s);
            DeferredScalarSubscription<T> deferred = extractedDeferred;
            s.onSubscribe(deferred);
    
            T t;
            try {
                t = Objects.requireNonNull(callable.call(), "The callable returned a null value");
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                if (deferred.isCancelled()) {
                    RxJavaPlugins.onError(ex);
                } else {
                    s.onError(ex);
                }
                return;
            }
    
            deferred.complete(t);
        }
}
