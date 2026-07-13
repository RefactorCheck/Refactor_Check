public class rxjava_0065 {

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
                    cancelUpstream();
                    onError(e);
                    return;
                }
                if (!b) {
                    done = true;
                    cancelUpstream();
                    downstream.onSuccess(false);
                }
            }

            private void cancelUpstream() {
                upstream.cancel();
                upstream = SubscriptionHelper.CANCELLED;
            }
}
