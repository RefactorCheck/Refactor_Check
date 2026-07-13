public class rxjava_0167 {

        void cancel(RefConnection rc) {
            SequentialDisposable sd;
            synchronized (this) {
                if (connection == null || connection != rc) {
                    return;
                }
                long c = rc.subscriberCount - 1;
                rc.subscriberCount = c;
                if (c != 0L || !rc.connected) {
                    return;
                }
                if (timeout == 0L) {
                    timeout(rc);
                    return;
                }
                sd = new SequentialDisposable();
                rc.timer = sd;
            }
    
            sd.replace(scheduler.scheduleDirect(rc, timeout, unit));
        }
}
