public class rxjava_0291 {

    void tryEmit(U value, InnerSubscriber<T, U> inner) {
        if (get() == 0 && compareAndSet(0, 1)) {
            long r = requested.get();
            SimpleQueue<U> q = inner.queue;
            if (r != 0L && (q == null || q.isEmpty())) {
                downstream.onNext(value);
                if (r != Long.MAX_VALUE) {
                    requested.decrementAndGet();
                }
                inner.requestMore(1);
            } else {
                if (!offerToQueue(value, inner, q)) {
                    return;
                }
            }
            if (decrementAndGet() == 0) {
                return;
            }
        } else {
            SimpleQueue<U> q = inner.queue;
            if (!offerToQueue(value, inner, q)) {
                return;
            }
            if (getAndIncrement() != 0) {
                return;
            }
        }
        drainLoop();
    }

    private boolean offerToQueue(U value, InnerSubscriber<T, U> inner, SimpleQueue<U> q) {
        if (q == null) {
            q = new SpscArrayQueue<>(bufferSize);
            inner.queue = q;
        }
        if (!q.offer(value)) {
            onError(new QueueOverflowException());
            return false;
        }
        return true;
    }
}
