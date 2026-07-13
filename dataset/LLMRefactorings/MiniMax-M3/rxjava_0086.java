public class rxjava_0086 {

    void drainFused() {
        int missed = 1;
        Subscriber<? super T> a = downstream;
        SimpleQueueWithConsumerIndex<Object> q = queue;

        for (;;) {
            if (handleCancelOrError(a, q)) {
                return;
            }

            boolean d = q.producerIndex() == sourceCount;

            if (!q.isEmpty()) {
                a.onNext(null);
            }

            if (d) {
                a.onComplete();
                return;
            }

            missed = addAndGet(-missed);
            if (missed == 0) {
                break;
            }
        }
    }

    private boolean handleCancelOrError(Subscriber<? super T> a, SimpleQueueWithConsumerIndex<Object> q) {
        if (cancelled) {
            q.clear();
            return true;
        }
        Throwable ex = errors.get();
        if (ex != null) {
            q.clear();
            a.onError(ex);
            return true;
        }
        return false;
    }
}
