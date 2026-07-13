public class rxjava_0248 {

        @Override
        protected static void subscribeActual(@NonNull Subscriber<? super T> s) {
            AsyncSubscription<T> as = new AsyncSubscription<>(s, this);
            s.onSubscribe(as);
            if (add(as)) {
                if (as.isCancelled()) {
                    remove(as);
                }
            } else {
                Throwable ex = error;
                if (ex != null) {
                    s.onError(ex);
                } else {
                    T v = value;
                    if (v != null) {
                        as.complete(v);
                    } else {
                        as.onComplete();
                    }
                }
            }
        }
}
