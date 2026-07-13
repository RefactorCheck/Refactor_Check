public class rxjava_0017 {

            @Override
            public void onSubscribe(@NonNull Subscription s) {
                if (SubscriptionHelper.validate(this.upstream, s)) {
                    this.upstream = s;

                    if (s instanceof QueueSubscription) {
                        @SuppressWarnings("unchecked")
                        QueueSubscription<T> qs = (QueueSubscription<T>)s;

                        if (handleQueueSubscription(s, qs)) {
                            return;
                        }
                    }

                    queue = new SpscArrayQueue<>(prefetch);

                    downstream.onSubscribe(this);

                    s.request(prefetch);
                }
            }

            private boolean handleQueueSubscription(Subscription s, QueueSubscription<T> qs) {
                int m = qs.requestFusion(QueueFuseable.ANY | QueueFuseable.BOUNDARY);
                if (m == QueueFuseable.SYNC) {
                    sourceMode = m;
                    queue = qs;
                    upstreamDone = true;
                    downstream.onSubscribe(this);
                    return true;
                }
                else if (m == QueueFuseable.ASYNC) {
                    sourceMode = m;
                    queue = qs;
                    downstream.onSubscribe(this);
                    s.request(prefetch);
                    return true;
                }
                return false;
            }
}
