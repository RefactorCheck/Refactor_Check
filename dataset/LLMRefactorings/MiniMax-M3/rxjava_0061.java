public class rxjava_0061 {

    void innerComplete(InnerObserver inner) {
        set.delete(inner);

        if (get() == 0 && compareAndSet(0, 1)) {
            if (tryTerminate()) {
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

    private boolean tryTerminate() {
        boolean d = active.decrementAndGet() == 0;
        SpscLinkedArrayQueue<R> q = queue.get();

        if (d && (q == null || q.isEmpty())) {
            errors.tryTerminateConsumer(downstream);
            return true;
        }
        return false;
    }
}
