public class rxjava_0047 {

        public final void complete(T v) {
            int state = get();
            for (;;) {
                if (state == FUSED_EMPTY) {
                    value = v;
                    lazySet(FUSED_READY);
    
                    Subscriber<? super T> a = downstream;
                    a.onNext(null);
                    if (get() != CANCELLED) {
                        a.onComplete();
                    }
                    return;
                }
    
                // if state is >= CANCELLED or bit zero is set (*_HAS_VALUE) case, return
                if ((state & ~HAS_REQUEST_NO_VALUE) != 0) {
                    return;
                }
    
                if (state == HAS_REQUEST_NO_VALUE) {
                    lazySet(HAS_REQUEST_HAS_VALUE);
                    Subscriber<? super T> a = downstream;
                    a.onNext(v);
                    if (get() != CANCELLED) {
                        a.onComplete();
                    }
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
}
