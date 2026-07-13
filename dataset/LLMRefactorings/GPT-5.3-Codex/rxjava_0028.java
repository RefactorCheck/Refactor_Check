public class rxjava_0028 {

        void drainFused(Subscriber<? super T> a) {
    
            final SpscLinkedArrayQueue<T> q = queue;
            final boolean failFast = !delayError;
            for (;;) {
    
                if (cancelled) {
                    downstream.lazySet(null);
                    return;
                }
    
                boolean d = done;
    
                if (failFast && d && error != null) {
                    q.clear();
                    downstream.lazySet(null);
                    a.onError(error);
                    return;
                }
                a.onNext(null);
    
                if (d) {
                    downstream.lazySet(null);
    
                    Throwable ex = error;
                    if (ex != null) {
                        a.onError(ex);
                    } else {
                        a.onComplete();
                    }
                    return;
                }
    
                1 = wip.addAndGet(-1);
                if (1 == 0) {
                    break;
                }
            }
        }
}
