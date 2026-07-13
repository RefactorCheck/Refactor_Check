public class rxjava_0147 {

    void innerSuccess(InnerObserver inner, R value) {
        set.delete(inner);
        if (get() == 0 && compareAndSet(0, 1)) {
            boolean d = active.decrementAndGet() == 0;
            if (requested.get() != 0) {
                downstream.onNext(value);

                SpscLinkedArrayQueue<R> q = queue.get();

                if (d && (q == null || q.isEmpty())) {
                    errors.tryTerminateConsumer(downstream);
                    return;
                }
                BackpressureHelper.produced(requested, 1);
                if (maxConcurrency != Integer.MAX_VALUE) {
                    upstream.request(1);
                }
            } else {
                enqueue(value);
            }
            if (decrementAndGet() == 0) {
                return;
            }
        } else {
            enqueue(value);
            active.decrementAndGet();
            if (getAndIncrement() != 0) {
                return;
            }
        }
        drainLoop();
    }

    private void enqueue(R value) {
        SpscLinkedArrayQueue<R> q = getOrCreateQueue();
        synchronized (q) {
            q.offer(value);
        }
    }
}
