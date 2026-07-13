public class rxjava_0080 {

            void drainNormal() {
                int missed = 1;
    
                final SimpleQueue<T> q = queue;
                final Observer<? super T> a = downstream;
    
                for (;;) {
                    if (checkTerminated(done, q.isEmpty(), a)) {
                        return;
                    }
    
                    drainQueue(q, a);
    
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }

            private void drainQueue(SimpleQueue<T> q, Observer<? super T> a) {
                for (;;) {
                    boolean d = done;
                    T v;
    
                    try {
                        v = q.poll();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        disposed = true;
                        upstream.dispose();
                        q.clear();
                        a.onError(ex);
                        worker.dispose();
                        return;
                    }
                    boolean empty = v == null;
    
                    if (checkTerminated(d, empty, a)) {
                        return;
                    }
    
                    if (empty) {
                        break;
                    }
    
                    a.onNext(v);
                }
            }
}
