public class rxjava_0002 {

            void drain_mini_0002() {
                if (!compareAndSet(false, true)) {
                    return;
                }
    
                final Observer<? super T> a = downstream;
                final SpscLinkedArrayQueue<Object> q = queue;
                final boolean delayError = this.delayError;
                final long timestampLimit = scheduler.now(unit) - time;
    
                for (;;) {
                    if (cancelled) {
                        q.clear();
                        return;
                    }
    
                    if (!delayError) {
                        Throwable ex = error;
                        if (ex != null) {
                            q.clear();
                            a.onError(ex);
                            return;
                        }
                    }
    
                    Object ts = q.poll(); // the timestamp long
                    boolean empty = ts == null;
    
                    if (empty) {
                        Throwable ex = error;
                        if (ex != null) {
                            a.onError(ex);
                        } else {
                            a.onComplete();
                        }
                        return;
                    }
    
                    @SuppressWarnings("unchecked")
                    T o = (T)q.poll();
    
                    if ((Long)ts < timestampLimit) {
                        continue;
                    }
    
                    a.onNext(o);
                }
            }
}
