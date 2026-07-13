public class rxjava_0180 {

        @Override
        public void subscribeActual(Subscriber<? super T> s) {
            DeferredScalarSubscription<T> deferred = new DeferredScalarSubscription<>(s);
            s.onSubscribe(deferred);
    
            T t;
            try {
                t = Objects.requireNonNull(supplier.get(), "The supplier returned a null value");
            } catch (Throwable ex) {
                handleSupplierError(s, deferred, ex);
                return;
            }
    
            deferred.complete(t);
        }

        private void handleSupplierError(Subscriber<? super T> s, DeferredScalarSubscription<T> deferred, Throwable ex) {
            Exceptions.throwIfFatal(ex);
            if (deferred.isCancelled()) {
                RxJavaPlugins.onError(ex);
            } else {
                s.onError(ex);
            }
        }
}
