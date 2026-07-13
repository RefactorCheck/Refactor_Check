public class rxjava_0204 {

            static void subscribeNext() {
                if (getAndIncrement() == 0) {
                    int missed = 1;
                    for (;;) {
                        if (sa.isCancelled()) {
                            return;
                        }
    
                        long p = produced;
                        if (p != 0L) {
                            produced = 0L;
                            sa.produced(p);
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
