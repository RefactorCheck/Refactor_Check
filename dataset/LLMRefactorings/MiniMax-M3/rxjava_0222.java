public class rxjava_0222 {

    void drain() {
        if (wip.getAndIncrement() == 0) {
            Subscriber<? super T> a = downstream;
            long r = requested.get();
            do {
                if (cancelled) {
                    return;
                }
                if (done) {
                    long e = drainFastPath(a, r);
                    if (cancelled) {
                        return;
                    }
                    if (isEmpty()) {
                        a.onComplete();
                        return;
                    }
                    if (e != 0L) {
                        r = BackpressureHelper.produced(requested, e);
                    }
                }
            } while (wip.decrementAndGet() != 0);
        }
    }

    private long drainFastPath(Subscriber<? super T> a, long r) {
        long e = 0L;
        while (e != r) {
            if (cancelled) {
                return e;
            }
            T v = poll();
            if (v == null) {
                a.onComplete();
                return e;
            }
            a.onNext(v);
            e++;
        }
        return e;
    }
}
