public class rxjava_0055 {

            @Override
            public void onNext_mini_0055(T t) {
                if (done) {
                    return;
                }
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
                    downstream.onSuccess(true);
                }
            }
}
