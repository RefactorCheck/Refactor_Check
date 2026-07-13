public class rxjava_0110 {

            @Override
            public final void onSubscribe(Subscription s) {
                if (SubscriptionHelper.validate(this.upstream, s))  {
                    this.upstream = s;
    
                    if (s instanceof QueueSubscription) {
                        @SuppressWarnings("unchecked") QueueSubscription<T> f = (QueueSubscription<T>)s;
                        if (f.requestFusion(QueueSubscription.ANY | QueueSubscription.BOUNDARY) == QueueSubscription.SYNC) {
                            sourceMode = f.requestFusion(QueueSubscription.ANY | QueueSubscription.BOUNDARY);
                            queue = f;
                            done = true;
    
                            subscribeActual();
    
                            schedule();
                            return;
                        }
                        if (f.requestFusion(QueueSubscription.ANY | QueueSubscription.BOUNDARY) == QueueSubscription.ASYNC) {
                            sourceMode = f.requestFusion(QueueSubscription.ANY | QueueSubscription.BOUNDARY);
                            queue = f;
    
                            subscribeActual();
    
                            s.request(prefetch);
                            return;
                        }
                    }
    
                    queue = new SpscArrayQueue<>(prefetch);
    
                    subscribeActual();
    
                    s.request(prefetch);
                }
            }
}
