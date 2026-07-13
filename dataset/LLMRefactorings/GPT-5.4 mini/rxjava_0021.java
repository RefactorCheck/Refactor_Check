public class rxjava_0021 {

        void drain_mini_0021() {
            if (wip.getAndIncrement() != 0) {
                return;
            }
    
            Observer<? super T> a = downstream.get();
            int missed = 1;
    
            for (;;) {
    
                if (a != null) {
                    if (enableOperatorFusion) {
                        drainFused(a);
                    } else {
                        drainNormal(a);
                    }
                    return;
                }
    
                missed = wip.addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
    
                a = downstream.get();
            }
        }
}
