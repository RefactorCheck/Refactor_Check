public class rxjava_0272 {

            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
                if (!test(t)) {
                    return;
                }
                downstream.onNext(t);
            }

            private boolean test(T t) {
                boolean b;
                try {
                    b = predicate.test(t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    upstream.dispose();
                    onError(e);
                    return false;
                }
                if (!b) {
                    done = true;
                    upstream.dispose();
                    downstream.onComplete();
                }
                return b;
            }
}
