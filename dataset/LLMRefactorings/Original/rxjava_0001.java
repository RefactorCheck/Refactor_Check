public class rxjava_0001 {

        public static <T, U> void drainMaxLoop(SimplePlainQueue<T> q, Subscriber<? super U> subscriber, boolean delayError,
                Disposable dispose, QueueDrain<T, U> qd) {
            int missed = 1;
    
            for (;;) {
                for (;;) {
                    boolean d = qd.done();
    
                    T v = q.poll();
    
                    boolean empty = v == null;
    
                    if (checkTerminated(d, empty, subscriber, delayError, q, qd)) {
                        if (dispose != null) {
                            dispose.dispose();
                        }
                        return;
                    }
    
                    if (empty) {
                        break;
                    }
    
                    long r = qd.requested();
                    if (r != 0L) {
                        if (qd.accept(subscriber, v)) {
                            if (r != Long.MAX_VALUE) {
                                qd.produced(1);
                            }
                        }
                    } else {
                        q.clear();
                        if (dispose != null) {
                            dispose.dispose();
                        }
                        subscriber.onError(MissingBackpressureException.createDefault());
                        return;
                    }
                }
    
                missed = qd.leave(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }
}
