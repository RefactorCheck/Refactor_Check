public class rxjava_0021 {

        void drain() {
            if (wip.getAndIncrement() != 0) {
                return;
            }
    
            int missed = 1;
    
            for (;;) {
    
                if (downstream.get() != null) {
                    if (enableOperatorFusion) {
                        drainFused(downstream.get());
                    } else {
                        drainNormal(downstream.get());
                    }
                    return;
                }
    
                missed = wip.addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
    
                downstream.get() = downstream.get();
            }
        }
}
