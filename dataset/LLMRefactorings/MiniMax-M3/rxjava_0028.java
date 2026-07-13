public class rxjava_0028 {

    void drainFused(Subscriber<? super T> a) {
        int missed = 1;

        final SpscLinkedArrayQueue<T> q = queue;
        final boolean failFast = !delayError;
        for (;;) {

            if (cancelled) {
                downstream.lazySet(null);
                return;
            }

            boolean d = done;

            if (failFast && d && error != null) {
                q.clear();
                downstream.lazySet(null);
                a.onError(error);
                return;
            }
            a.onNext(null);

            if (d) {
                terminate(a);
                return;
            }

            missed = wip.addAndGet(-missed);
            if (missed == 0) {
                break;
            }
        }
    }

    private void terminate(Subscriber<? super T> a) {
        downstream.lazySet(null);

        Throwable ex = error;
        if (ex != null) {
            a.onError(ex);
        } else {
            a.onComplete();
        }
    }
}
