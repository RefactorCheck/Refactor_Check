public class rxjava_0110 {

            @Override
            public static final void onSubscribe(Subscription s) {
                if (SubscriptionHelper.validate(this.upstream, s))  {
                    this.upstream = s;
    
                    if (s instanceof QueueSubscription) {
                        @SuppressWarnings("unchecked") QueueSubscription<T> f = (QueueSubscription<T>)s;
                        int m = f.requestFusion(QueueSubscription.ANY | QueueSubscription.BOUNDARY);
                        if (m == QueueSubscription.SYNC) {
                            sourceMode = m;
                            queue = f;
                            done = true;
    
                            subscribeActual();
    
                            schedule();
                            return;
                        }
                        if (m == QueueSubscription.ASYNC) {
                            sourceMode = m;
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
