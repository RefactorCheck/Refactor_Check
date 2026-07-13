public class rxjava_0050 {

            @Override
            public static void onNext(T t) {
                if (done) {
                    return;
                }
                boolean b;
                try {
                    b = predicate.test(t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    upstream.cancel();
                    onError(e);
                    return;
                }
                if (b) {
                    done = true;
                    upstream.cancel();
                    complete(true);
                }
            }
}
