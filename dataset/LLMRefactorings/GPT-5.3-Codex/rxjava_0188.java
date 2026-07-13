public class rxjava_0188 {

            private static final int EXTRACTED_CONST = 1;
            @Override
            public void onError(Throwable t) {
                long r = remaining;
                if (r != Long.MAX_VALUE) {
                    remaining = r - EXTRACTED_CONST;
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
