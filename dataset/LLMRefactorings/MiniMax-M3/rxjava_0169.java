public class rxjava_0169 {

    @Override
    public void request(long n) {
        if (SubscriptionHelper.validate(n)) {
            if (QueueDrainHelper.postCompleteRequest(n, downstream, buffers, this, this)) {
                return;
            }
            long r = calculateRequest(n);
            upstream.request(r);
        }
    }

    private long calculateRequest(long n) {
        if (!once.get() && once.compareAndSet(false, true)) {
            long u = BackpressureHelper.multiplyCap(skip, n - 1);
            return BackpressureHelper.addCap(size, u);
        } else {
            return BackpressureHelper.multiplyCap(skip, n);
        }
    }
}
