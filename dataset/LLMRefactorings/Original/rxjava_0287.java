public class rxjava_0287 {

            @Override
            void runBackfused() {
                int missed = 1;
    
                for (;;) {
    
                    if (cancelled) {
                        return;
                    }
    
                    boolean d = done;
    
                    downstream.onNext(null);
    
                    if (d) {
                        cancelled = true;
                        Throwable e = error;
                        if (e != null) {
                            downstream.onError(e);
                        } else {
                            downstream.onComplete();
                        }
                        worker.dispose();
                        return;
                    }
    
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }
}
