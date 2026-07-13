public class rxjava_0300 {

        @Override
        public void connect(Consumer<? super Disposable> connection) {
            boolean doConnect = false;
            PublishConnection<T> conn;
    
            for (;;) {
                conn = current.get();
    
                if (conn == null || conn.isDisposed()) {
                    PublishConnection<T> fresh = new PublishConnection<>(current);
                    if (!current.compareAndSet(conn, fresh)) {
                        continue;
                    }
                    conn = fresh;
                }
    
                doConnect = !conn.connect.get() && conn.connect.compareAndSet(false, true);
                break;
            }
    
            try {
                connection.accept(conn);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                throw ExceptionHelper.wrapOrThrow(ex);
            }
    
            if (doConnect) {
                source.subscribe(conn);
            }
        }
}
