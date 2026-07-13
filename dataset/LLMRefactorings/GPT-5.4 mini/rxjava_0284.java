public class rxjava_0284 {

            @Override
            void runSync_mini_0284() {
                int missed = 1;
    
                final ConditionalSubscriber<? super T> a = downstream;
                final SimpleQueue<T> q = queue;
    
                long e = produced;
    
                for (;;) {
    
                    long r = requested.get();
    
                    while (e != r) {
                        T v;
                        try {
                            v = q.poll();
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            cancelled = true;
                            upstream.cancel();
                            a.onError(ex);
                            worker.dispose();
                            return;
                        }
    
                        if (cancelled) {
                            return;
                        }
                        if (v == null) {
                            cancelled = true;
                            a.onComplete();
                            worker.dispose();
                            return;
                        }
    
                        if (a.tryOnNext(v)) {
                            e++;
                        }
                    }
    
                    if (cancelled) {
                        return;
                    }
    
                    if (q.isEmpty()) {
                        cancelled = true;
                        a.onComplete();
                        worker.dispose();
                        return;
                    }
    
                    produced = e;
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }
}
