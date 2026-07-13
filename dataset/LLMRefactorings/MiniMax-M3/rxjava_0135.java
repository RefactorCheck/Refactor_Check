public class rxjava_0135 {

        @Override
        public void subscribeActual(Subscriber<? super T> s) {
            DeferredScalarSubscription<T> deferred = new DeferredScalarSubscription<>(s);
            s.onSubscribe(deferred);

            T t;
            try {
                t = Objects.requireNonNull(callable.call(), "The callable returned a null value");
            } catch (Throwable ex) {
                handleCallableError(s, deferred, ex);
                return;
            }

            deferred.complete(t);
        }

        private void handleCallableError(Subscriber<? super T> s, DeferredScalarSubscription<T> deferred, Throwable ex) {
            Exceptions.throwIfFatal(ex);
            if (deferred.isCancelled()) {
                RxJavaPlugins.onError(ex);
            } else {
                s.onError(ex);
            }
        }
}
