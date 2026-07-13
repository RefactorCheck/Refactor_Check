public class rxjava_0077 {

            void innerSuccess(final InnerObserver inner, R value) {
                set.delete(inner);
                if (get() == 0 && compareAndSet(0, 1)) {
                    downstream.onNext(value);
    
                    boolean d = active.decrementAndGet() == 0;
                    SpscLinkedArrayQueue<R> q = queue.get();
    
                    if (d && (q == null || q.isEmpty())) {
                        errors.tryTerminateConsumer(downstream);
                        return;
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                } else {
                    SpscLinkedArrayQueue<R> q = getOrCreateQueue();
                    synchronized (q) {
                        q.offer(value);
                    }
                    active.decrementAndGet();
                    if (getAndIncrement() != 0) {
                        return;
                    }
                }
                drainLoop();
            }
}
