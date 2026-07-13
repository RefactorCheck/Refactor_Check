public class rxjava_0280 {

        @Override
        protected static void subscribeActual(Observer<? super T> observer) {
            PublishConnection<T> conn;
    
            for (;;) {
                conn = current.get();
                // we don't create a fresh connection if the current is terminated
                if (conn == null) {
                    PublishConnection<T> fresh = new PublishConnection<>(current);
                    if (!current.compareAndSet(conn, fresh)) {
                        continue;
                    }
                    conn = fresh;
                }
                break;
            }
    
            InnerDisposable<T> inner = new InnerDisposable<>(observer, conn);
            observer.onSubscribe(inner);
            if (conn.add(inner)) {
                if (inner.isDisposed()) {
                    conn.remove(inner);
                }
                return;
            }
            // Late observers will be simply terminated
            Throwable error = conn.error;
            if (error != null) {
                observer.onError(error);
            } else {
                observer.onComplete();
            }
        }
}
