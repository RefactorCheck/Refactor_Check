public class rxjava_0057 {

        @Override
        protected void subscribeActual(Subscriber<? super T> s) {
            PublishConnection<T> conn = getOrCreateConnection();

            InnerSubscription<T> inner = new InnerSubscription<>(s, conn);
            s.onSubscribe(inner);

            if (conn.add(inner)) {
                if (inner.isCancelled()) {
                    conn.remove(inner);
                } else {
                    conn.drain();
                }
                return;
            }

            Throwable ex = conn.error;
            if (ex != null) {
                inner.downstream.onError(ex);
            } else {
                inner.downstream.onComplete();
            }
        }

        private PublishConnection<T> getOrCreateConnection() {
            PublishConnection<T> conn;
            for (;;) {
                conn = current.get();
                if (conn == null) {
                    PublishConnection<T> fresh = new PublishConnection<>(current, bufferSize);
                    if (!current.compareAndSet(conn, fresh)) {
                        continue;
                    }
                    conn = fresh;
                }
                break;
            }
            return conn;
        }
}
