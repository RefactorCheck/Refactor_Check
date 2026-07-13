public class rxjava_0287 {

            @Override
            void runBackfused() {
    
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
    
                    1 = addAndGet(-1);
                    if (1 == 0) {
                        break;
                    }
                }
            }
}
