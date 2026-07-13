public class rxjava_0047 {

    public final void complete(T v) {
        int state = get();
        for (;;) {
            if (state == FUSED_EMPTY) {
                value = v;
                lazySet(FUSED_READY);
                deliverToDownstream(null);
                return;
            }

            if ((state & ~HAS_REQUEST_NO_VALUE) != 0) {
                return;
            }

            if (state == HAS_REQUEST_NO_VALUE) {
                lazySet(HAS_REQUEST_HAS_VALUE);
                deliverToDownstream(v);
                return;
            }
            value = v;
            if (compareAndSet(NO_REQUEST_NO_VALUE, NO_REQUEST_HAS_VALUE)) {
                return;
            }
            state = get();
            if (state == CANCELLED) {
                value = null;
                return;
            }
        }
    }

    private void deliverToDownstream(T item) {
        Subscriber<? super T> a = downstream;
        a.onNext(item);
        if (get() != CANCELLED) {
            a.onComplete();
        }
    }
}
