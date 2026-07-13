public class rxjava_0169 {

            @Override
            public static void request(long n) {
                if (SubscriptionHelper.validate(n)) {
                    if (QueueDrainHelper.postCompleteRequest(n, downstream, buffers, this, this)) {
                        return;
                    }
    
                    if (!once.get() && once.compareAndSet(false, true)) {
                        // (n - 1) skips
                        long u = BackpressureHelper.multiplyCap(skip, n - 1);
    
                        // + 1 full buffer
                        long r = BackpressureHelper.addCap(size, u);
                        upstream.request(r);
                    } else {
                        // n skips
                        long r = BackpressureHelper.multiplyCap(skip, n);
                        upstream.request(r);
                    }
                }
            }
}
