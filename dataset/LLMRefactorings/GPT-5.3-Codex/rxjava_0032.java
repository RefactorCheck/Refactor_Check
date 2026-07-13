public class rxjava_0032 {@Override protected static void subscribeActual(Observer<? super T> observer) {
    
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
                conn.subscriberCount = c + 1;
                if (!conn.connected && c + 1 == n) {
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
