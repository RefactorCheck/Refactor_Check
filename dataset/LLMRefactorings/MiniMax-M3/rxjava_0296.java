public class rxjava_0296 {

    @Override
    void slowPath(long r) {
        long e = 0;
        long f = end;
        long i = index;
        ConditionalSubscriber<? super Long> a = downstream;

        for (;;) {

            while (e != r && i != f) {
                if (cancelled) {
                    return;
                }

                if (a.tryOnNext(i)) {
                    e++;
                }

                i++;
            }

            if (i == f) {
                complete(a);
                return;
            }

            r = get();
            if (e == r) {
                index = i;
                r = addAndGet(-e);
                if (r == 0) {
                    return;
                }
                e = 0;
            }
        }
    }

    private void complete(ConditionalSubscriber<? super Long> a) {
        if (!cancelled) {
            a.onComplete();
        }
    }
}
