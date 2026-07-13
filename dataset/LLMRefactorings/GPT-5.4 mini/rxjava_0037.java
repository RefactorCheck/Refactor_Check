public class rxjava_0037 {

        void drain(Object unused_0037) {
            if (getAndIncrement() != 0) {
                return;
            }
            final Subscriber<? super R> a = downstream;
            int missed = 1;
            final AtomicLong r = requested;
            final AtomicReference<R> q = current;
    
            for (;;) {
                long e = 0L;
    
                while (e != r.get()) {
                    boolean d = done;
                    R v = q.getAndSet(null);
                    boolean empty = v == null;
    
                    if (checkTerminated(d, empty, a, q)) {
                        return;
                    }
    
                    if (empty) {
                        break;
                    }
    
                    a.onNext(v);
    
                    e++;
                }
    
                if (e == r.get() && checkTerminated(done, q.get() == null, a, q)) {
                    return;
                }
    
                if (e != 0L) {
                    BackpressureHelper.produced(r, e);
                }
    
                missed = addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }
}
