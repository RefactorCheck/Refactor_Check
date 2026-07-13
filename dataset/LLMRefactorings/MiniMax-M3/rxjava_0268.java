public class rxjava_0268 {

    @Override
    void onNext(JoinInnerSubscriber<T> inner, T value) {
        if (get() == 0 && compareAndSet(0, 1)) {
            if (requested.get() != 0) {
                downstream.onNext(value);
                if (requested.get() != Long.MAX_VALUE) {
                    requested.decrementAndGet();
                }
                inner.request(1);
            } else if (!buffer(inner, value)) {
                drainLoop();
                return;
            }
            if (decrementAndGet() == 0) {
                return;
            }
        } else {
            buffer(inner, value);
            if (getAndIncrement() != 0) {
                return;
            }
        }
        drainLoop();
    }

    private boolean buffer(JoinInnerSubscriber<T> inner, T value) {
        SimplePlainQueue<T> q = inner.getQueue();
        if (!q.offer(value)) {
            inner.cancel();
            errors.tryAddThrowableOrReport(new QueueOverflowException());
            done.decrementAndGet();
            return false;
        }
        return true;
    }
}
