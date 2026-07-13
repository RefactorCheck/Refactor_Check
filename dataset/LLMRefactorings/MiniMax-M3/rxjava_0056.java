public class rxjava_0056 {

            @Override
            public void onNext(T t) {
                if (!done) {
                    downstream.onNext(t);
                    try {
                        if (predicate.test(t)) {
                            done = true;
                            upstream.dispose();
                            downstream.onComplete();
                        }
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        upstream.dispose();
                        onError(e);
                    }
                }
            }
}
