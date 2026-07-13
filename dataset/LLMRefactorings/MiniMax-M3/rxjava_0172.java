public class rxjava_0172 {

    void drainLoop() {
        int missed = 1;
        Observer<? super R> actual = downstream;
        AtomicInteger n = active;
        AtomicReference<SpscLinkedArrayQueue<R>> queueRef = queue;

        for (;;) {
            for (;;) {
                if (cancelled) {
                    clear();
                    return;
                }

                if (!delayErrors) {
                    Throwable error = errors.get();
                    if (error != null) {
                        clear();
                        errors.tryTerminateConsumer(actual);
                        return;
                    }
                }

                boolean done = n.get() == 0;
                SpscLinkedArrayQueue<R> queue = queueRef.get();
                R value = queue != null ? queue.poll() : null;
                boolean empty = value == null;

                if (done && empty) {
                    errors.tryTerminateConsumer(downstream);
                    return;
                }

                if (empty) {
                    break;
                }

                actual.onNext(value);
            }

            missed = addAndGet(-missed);
            if (missed == 0) {
                break;
            }
        }
    }
}
