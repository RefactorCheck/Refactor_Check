public class rxjava_0032 {

        @Override
        protected void subscribeActual(Observer<? super T> observer) {
    
            RefConnection conn;
    
            boolean connect = false;
            synchronized (this) {
                conn = connection;
                if (conn == null) {
                    conn = new RefConnection(this);
                    connection = conn;
                }
    
                long c = conn.subscriberCount;
                if (c == 0L && conn.timer != null) {
                    conn.timer.dispose();
                }
                long newCount = c + 1;
                conn.subscriberCount = newCount;
                if (!conn.connected && newCount == n) {
                    connect = true;
                    conn.connected = true;
                }
            }
    
            source.subscribe(new RefCountObserver<>(observer, this, conn));
    
            if (connect) {
                source.connect(conn);
            }
        }
}
