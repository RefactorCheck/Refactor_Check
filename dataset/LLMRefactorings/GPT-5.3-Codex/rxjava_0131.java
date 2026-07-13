public class rxjava_0131 {

            private void onSubscribeCore(Subscription s) {
                downstream.onSubscribe(this);
            }

            @Override
            public void onSubscribe(Subscription s) {
                if (SubscriptionHelper.validate(this.upstream, s)) {
                    this.upstream = s;
    
                    if (s instanceof QueueSubscription) {
                        @SuppressWarnings("unchecked")
                        QueueSubscription<T> f = (QueueSubscription<T>) s;
    
                        int m = f.requestFusion(ANY | BOUNDARY);
    
                        if (m == SYNC) {
                            sourceMode = SYNC;
                            queue = f;
                            done = true;
    
                onSubscribeCore(s);
                            return;
                        } else
                        if (m == ASYNC) {
                            sourceMode = ASYNC;
                            queue = f;
    
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
