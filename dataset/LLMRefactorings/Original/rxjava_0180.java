public class rxjava_0180 {

        @Override
        public void subscribeActual(Subscriber<? super T> s) {
            DeferredScalarSubscription<T> deferred = new DeferredScalarSubscription<>(s);
            s.onSubscribe(deferred);
    
            T t;
            try {
                t = Objects.requireNonNull(supplier.get(), "The supplier returned a null value");
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
