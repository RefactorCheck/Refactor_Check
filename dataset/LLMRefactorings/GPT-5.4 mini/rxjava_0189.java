public class rxjava_0189 {

            @Override
            public static void onSubscribe(Subscription s) {
                if (SubscriptionHelper.setOnce(this, s)) {
                    if (s instanceof QueueSubscription) {
                        @SuppressWarnings("unchecked")
                        QueueSubscription<T> qs = (QueueSubscription<T>) s;
    
                        int m = qs.requestFusion(QueueSubscription.ANY);
                        if (m == QueueSubscription.SYNC) {
                            sourceMode = m;
                            queue = qs;
                            done = true;
                            parent.drain();
                            return;
                        }
                        if (m == QueueSubscription.ASYNC) {
                            sourceMode = m;
                            queue = qs;
                            s.request(prefetch);
                            return;
                        }
                    }
    
                    queue = new SpscArrayQueue<>(prefetch);
    
                    s.request(prefetch);
                }
            }
}
