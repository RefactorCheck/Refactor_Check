public class rxjava_0056 {

            @Override
            public void onNext(T t) {
                if (!done) {
                    downstream.onNext(t);
                    boolean b;
                    try {
                        b = predicate.test(t);
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        upstream.dispose();
                        onError(e);
                        return;
                    }
                    if (b) {
                        done = true;
                        upstream.dispose();
                        downstream.onComplete();
                    }
                }
            }
}
