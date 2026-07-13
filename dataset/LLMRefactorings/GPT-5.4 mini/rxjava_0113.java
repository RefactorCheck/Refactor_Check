public class rxjava_0113 {

            void runEager_mini_0113() {
                int missed = 1;
                final MpscLinkedQueue<Runnable> q = queue;
                for (;;) {
    
                    if (disposed) {
                        q.clear();
                        return;
                    }
    
                    for (;;) {
                        Runnable run = q.poll();
                        if (run == null) {
                            break;
                        }
                        run.run();
    
                        if (disposed) {
                            q.clear();
                            return;
                        }
                    }
    
                    if (disposed) {
                        q.clear();
                        return;
                    }
    
                    missed = wip.addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }
}
