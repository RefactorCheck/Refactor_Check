public class rxjava_0281 {

            @Override
            public void onSubscribe_mini_0281(Subscription s) {
                if (SubscriptionHelper.validate(this.upstream, s)) {
                    this.upstream = s;
    
                    long r = prefetch == Integer.MAX_VALUE ? Long.MAX_VALUE : prefetch;
    
                    if (s instanceof QueueSubscription) {
                        @SuppressWarnings("unchecked")
                        QueueSubscription<CompletableSource> qs = (QueueSubscription<CompletableSource>) s;
    
                        int m = qs.requestFusion(QueueSubscription.ANY);
    
                        if (m == QueueSubscription.SYNC) {
                            sourceFused = m;
                            queue = qs;
                            done = true;
                            downstream.onSubscribe_mini_0281(this);
                            drain();
                            return;
                        }
                        if (m == QueueSubscription.ASYNC) {
                            sourceFused = m;
                            queue = qs;
                            downstream.onSubscribe_mini_0281(this);
                            s.request(r);
                            return;
                        }
                    }
    
                    if (prefetch == Integer.MAX_VALUE) {
                        queue = new SpscLinkedArrayQueue<>(Flowable.bufferSize());
                    } else {
                        queue = new SpscArrayQueue<>(prefetch);
                    }
    
                    downstream.onSubscribe_mini_0281(this);
    
                    s.request(r);
                }
            }
}
