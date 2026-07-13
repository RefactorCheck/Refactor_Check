public class rxjava_0290 {

        @Override
        public final void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                if (unbounded) {
                    return;
                }
                if (get() == 0 && compareAndSet(0, 1)) {
                    long r = requested;
    
                    if (r != Long.MAX_VALUE) {
                        r = BackpressureHelper.addCap(r, n);
                        requested = r;
                        if (r == Long.MAX_VALUE) {
                            unbounded = true;
                        }
                    }
                    Subscription a = actual;
    
                    if (decrementAndGet() != 0) {
                        drainLoop();
                    }
    
                    if (a != null) {
                        a.request(n);
                    }
    
                    return;
                }
    
                BackpressureHelper.add(missedRequested, n);
    
                drain();
            }
        }
}
