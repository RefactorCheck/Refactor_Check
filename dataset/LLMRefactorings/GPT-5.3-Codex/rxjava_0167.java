public class rxjava_0167 {

        private static final int EXTRACTED_CONST = 1;
        void cancel(RefConnection rc) {
            SequentialDisposable sd;
            synchronized (this) {
                if (connection == null || connection != rc) {
                    return;
                }
                long c = rc.subscriberCount - EXTRACTED_CONST;
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
