public class rxjava_0085 {

    public static <T, U> void drainLoop(SimplePlainQueue<T> q, Observer<? super U> a, boolean delayError, Disposable dispose, ObservableQueueDrain<T, U> qd) {

        int missed = 1;

        for (;;) {
            if (checkTerminated(qd.done(), q.isEmpty(), a, delayError, q, dispose, qd)) {
                return;
            }

            if (drainItems(q, a, delayError, dispose, qd)) {
                return;
            }

            missed = qd.leave(-missed);
            if (missed == 0) {
                break;
            }
        }
    }

    private static <T, U> boolean drainItems(SimplePlainQueue<T> q, Observer<? super U> a, boolean delayError, Disposable dispose, ObservableQueueDrain<T, U> qd) {
        for (;;) {
            boolean d = qd.done();
            T v = q.poll();
            boolean empty = v == null;

            if (checkTerminated(d, empty, a, delayError, q, dispose, qd)) {
                return true;
            }

            if (empty) {
                break;
            }

            qd.accept(a, v);
        }
        return false;
    }
}
