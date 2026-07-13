public class rxjava_0241 {

            void drainFused() {
                int missed = 1;
    
                for (;;) {
                    if (disposed) {
                        return;
                    }
    
                    boolean d = done;
                    Throwable ex = error;
    
                    if (!delayError && d && ex != null) {
                        disposed = true;
                        downstream.onError(error);
                        worker.dispose();
                        return;
                    }
    
                    downstream.onNext(null);
    
                    if (d) {
                        terminate();
                        return;
                    }
    
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }
    
            private void terminate() {
                disposed = true;
                Throwable ex = error;
                if (ex != null) {
                    downstream.onError(ex);
                } else {
                    downstream.onComplete();
                }
                worker.dispose();
            }
}
