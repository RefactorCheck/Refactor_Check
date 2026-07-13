public class rxjava_0290 {

        @Override
        public final void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                if (unbounded) {
                    return;
                }
                if (get() == 0 && compareAndSet(0, 1)) {
    
                    if (requested != Long.MAX_VALUE) {
                        requested = BackpressureHelper.addCap(requested, n);
                        requested = requested;
                        if (requested == Long.MAX_VALUE) {
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
