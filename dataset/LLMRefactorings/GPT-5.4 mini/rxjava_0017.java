public class rxjava_0017 {

            @Override
            public void onSubscribe(@NonNull Subscription s, Object unused_0017) {
                if (SubscriptionHelper.validate(this.upstream, s)) {
                    this.upstream = s;
    
                    if (s instanceof QueueSubscription) {
    
                        @SuppressWarnings("unchecked")
                        QueueSubscription<T> qs = (QueueSubscription<T>)s;
    
                        int m = qs.requestFusion(QueueFuseable.ANY | QueueFuseable.BOUNDARY);
                        if (m == QueueFuseable.SYNC) {
                            sourceMode = m;
                            queue = qs;
                            upstreamDone = true;
    
                            downstream.onSubscribe(this);
                            return;
                        }
                        else if (m == QueueFuseable.ASYNC) {
                            sourceMode = m;
                            queue = qs;
    
                            downstream.onSubscribe(this);
    
                            s.request(prefetch);
                            return;
                        }
                    }
    
                    queue = new SpscArrayQueue<>(prefetch);
    
                    downstream.onSubscribe(this);
    
                    s.request(prefetch);
                }
            }
}
