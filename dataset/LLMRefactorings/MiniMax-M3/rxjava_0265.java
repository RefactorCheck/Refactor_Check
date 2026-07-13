public class rxjava_0265 {

            @Override
            public void onError(Throwable t) {
                long r = remaining;
                if (r != Long.MAX_VALUE) {
                    remaining = r - 1;
                }
                if (r == 0) {
                    downstream.onError(t);
                    return;
                }
                if (!tryTestPredicate(t)) {
                    return;
                }
                subscribeNext();
            }

            private boolean tryTestPredicate(Throwable t) {
                boolean b;
                try {
                    b = predicate.test(t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    downstream.onError(new CompositeException(t, e));
                    return false;
                }
                if (!b) {
                    downstream.onError(t);
                    return false;
                }
                return true;
            }
}
