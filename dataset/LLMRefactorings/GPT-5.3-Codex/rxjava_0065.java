public class rxjava_0065 {

            private  b;
            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
                boolean b;
                try {
                  b = predicate.test(t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    upstream.cancel();
                    upstream = SubscriptionHelper.CANCELLED;
                    onError(e);
                    return;
                }
                if (!b) {
                    done = true;
                    upstream.cancel();
                    upstream = SubscriptionHelper.CANCELLED;
                    downstream.onSuccess(false);
                }
            }
}
