public class rxjava_0148<T> {

    void drainLoop() {
        ObservableEmitter<T> e = emitter;
        SpscLinkedArrayQueue<T> q = queue;
        AtomicThrowable errors = this.errors;
        int missed = 1;
        for (;;) {
            if (drain(e, q, errors)) {
                return;
            }

            missed = addAndGet(-missed);
            if (missed == 0) {
                break;
            }
        }
    }

    private boolean drain(ObservableEmitter<T> e, SpscLinkedArrayQueue<T> q, AtomicThrowable errors) {
        for (;;) {
            if (e.isDisposed()) {
                q.clear();
                return true;
            }

            if (errors.get() != null) {
                q.clear();
                errors.tryTerminateConsumer(e);
                return true;
            }

            boolean d = done;
            T v = q.poll();

            boolean empty = v == null;

            if (d && empty) {
                e.onComplete();
                return true;
            }

            if (empty) {
                return false;
            }

            e.onNext(v);
        }
    }
}
