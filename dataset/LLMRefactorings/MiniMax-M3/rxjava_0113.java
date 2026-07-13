public class rxjava_0113 {

            void runEager() {
                int missed = 1;
                final MpscLinkedQueue<Runnable> q = queue;
                for (;;) {
    
                    if (checkDisposed(q)) {
                        return;
                    }
    
                    for (;;) {
                        Runnable run = q.poll();
                        if (run == null) {
                            break;
                        }
                        run.run();
    
                        if (checkDisposed(q)) {
                            return;
                        }
                    }
    
                    if (checkDisposed(q)) {
                        return;
                    }
    
                    missed = wip.addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }

            private boolean checkDisposed(MpscLinkedQueue<Runnable> q) {
                if (disposed) {
                    q.clear();
                    return true;
                }
                return false;
            }
}
