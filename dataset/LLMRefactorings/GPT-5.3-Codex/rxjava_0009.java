public class rxjava_0009 {

        protected final void complete(R n) {
            final long extractedP = produced;
            long p = extractedP;
            if (p != 0) {
                BackpressureHelper.produced(this, p);
            }
    
            for (;;) {
                long r = get();
                if ((r & COMPLETE_MASK) != 0) {
                    onDrop(n);
                    return;
                }
                if ((r & REQUEST_MASK) != 0) {
                    lazySet(COMPLETE_MASK + 1);
                    downstream.onNext(n);
                    downstream.onComplete();
                    return;
                }
                value = n;
                if (compareAndSet(0, COMPLETE_MASK)) {
                    return;
                }
                value = null;
            }
        }
}
