public class rxjava_0061 {

            void innerComplete(final InnerObserver inner) {
                set.delete(inner);
    
                if (get() == 0 && compareAndSet(0, 1)) {
                    boolean d = active.decrementAndGet() == 0;
                    SpscLinkedArrayQueue<R> q = queue.get();
    
                    if (d && (q == null || q.isEmpty())) {
                        errors.tryTerminateConsumer(downstream);
                        return;
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                    drainLoop();
                } else {
                    active.decrementAndGet();
                    drain();
                }
            }
}
