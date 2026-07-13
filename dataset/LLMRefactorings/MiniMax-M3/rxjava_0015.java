public class rxjava_0015 {
    void innerComplete(InnerObserver inner) {
        set.delete(inner);

        if (get() == 0 && compareAndSet(0, 1)) {
            boolean d = active.decrementAndGet() == 0;
            SpscLinkedArrayQueue<R> q = queue.get();

            if (checkTerminate(d, q)) {
                errors.tryTerminateConsumer(downstream);
                return;
            }

            requestOne();
            if (decrementAndGet() == 0) {
                return;
            }
            drainLoop();
        } else {
            active.decrementAndGet();
            requestOne();
            drain();
        }
    }

    private void requestOne() {
        if (maxConcurrency != Integer.MAX_VALUE) {
            upstream.request(1);
        }
    }
}
