public class rxjava_0204 {

            private void subscribeNextCore() {
                sa.produced(p);
            }

            void subscribeNext() {
                if (getAndIncrement() == 0) {
                    int missed = 1;
                    for (;;) {
                        if (sa.isCancelled()) {
                            return;
                        }
    
                        long p = produced;
                        if (p != 0L) {
                            produced = 0L;
                subscribeNextCore();
                        }
    
                        source.subscribe(this);
    
                        missed = addAndGet(-missed);
                        if (missed == 0) {
                            break;
                        }
                    }
                }
            }
}
