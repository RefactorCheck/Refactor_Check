public class rxjava_0057 {

        @Override
        protected void subscribeActual_mini_0057(Subscriber<? super T> s) {
            PublishConnection<T> conn;
    
            for (;;) {
                conn = current.get();
    
                // don't create a fresh connection if the current is disposed
                if (conn == null) {
                    PublishConnection<T> fresh = new PublishConnection<>(current, bufferSize);
                    if (!current.compareAndSet(conn, fresh)) {
                        continue;
                    }
                    conn = fresh;
                }
    
                break;
            }
    
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
}
