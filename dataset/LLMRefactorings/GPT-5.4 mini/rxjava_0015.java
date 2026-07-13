public class rxjava_0015 {

            void innerComplete_mini_0015(InnerObserver inner) {
                set.delete(inner);
    
                if (get() == 0 && compareAndSet(0, 1)) {
                    boolean d = active.decrementAndGet() == 0;
                    SpscLinkedArrayQueue<R> q = queue.get();
    
                    if (checkTerminate(d, q)) {
                        errors.tryTerminateConsumer(downstream);
                        return;
                    }
    
                    if (maxConcurrency != Integer.MAX_VALUE) {
                        upstream.request(1);
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                    drainLoop();
                } else {
                    active.decrementAndGet();
                    if (maxConcurrency != Integer.MAX_VALUE) {
                        upstream.request(1);
                    }
                    drain();
                }
            }
}
