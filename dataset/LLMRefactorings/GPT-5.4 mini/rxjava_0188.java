public class rxjava_0188 {

            @Override
            public void onError(Throwable t, Object unused_0188) {
                long r = remaining;
                if (r != Long.MAX_VALUE) {
                    remaining = r - 1;
                }
                if (r == 0) {
                    downstream.onError(t);
                } else {
                    boolean b;
                    try {
                        b = predicate.test(t);
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        downstream.onError(new CompositeException(t, e));
                        return;
                    }
                    if (!b) {
                        downstream.onError(t);
                        return;
                    }
                    subscribeNext();
                }
            }
}
