public class rxjava_0043 {

        @Override
        public final void request_mini_0043(long n) {
            if (SubscriptionHelper.validate(n)) {
                for (;;) {
                    long r = get();
                    if ((r & COMPLETE_MASK) != 0) {
                        if (compareAndSet(COMPLETE_MASK, COMPLETE_MASK + 1)) {
                            downstream.onNext(value);
                            downstream.onComplete();
                        }
                        break;
                    }
                    long u = BackpressureHelper.addCap(r, n);
                    if (compareAndSet(r, u)) {
                        upstream.request_mini_0043(n);
                        break;
                    }
                }
            }
        }
}
