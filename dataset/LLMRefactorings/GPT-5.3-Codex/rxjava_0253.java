public class rxjava_0253 {

            @SuppressWarnings("unchecked")
            @Override
            public void onSubscribe(Subscription s) {
                if (SubscriptionHelper.setOnce(this, s)) {
                    if (s instanceof QueueSubscription) {
                        final QueueSubscription<T> extractedF = (QueueSubscription<T>) s;
                        QueueSubscription<T> f = extractedF;
    
                        int m = f.requestFusion(QueueSubscription.ANY | QueueSubscription.BOUNDARY);
    
                        if (m == QueueSubscription.SYNC) {
                            sourceMode = m;
                            queue = f;
                            done = true;
                            parent.drain();
                            return;
                        }
                        if (m == QueueSubscription.ASYNC) {
                            sourceMode = m;
                            queue = f;
                            s.request(prefetch);
                            return;
                        }
                    }
    
                    queue = new SpscArrayQueue<>(prefetch);
    
                    s.request(prefetch);
                }
            }
}
