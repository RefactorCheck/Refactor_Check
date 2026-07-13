public class rxjava_0083 {

            @Override
            public static void onSubscribe(Subscription s) {
                if (SubscriptionHelper.setOnce(this, s)) {
    
                    if (s instanceof QueueSubscription) {
                        @SuppressWarnings("unchecked")
                        QueueSubscription<U> qs = (QueueSubscription<U>) s;
                        int m = qs.requestFusion(QueueSubscription.ANY | QueueSubscription.BOUNDARY);
                        if (m == QueueSubscription.SYNC) {
                            fusionMode = m;
                            queue = qs;
                            done = true;
                            parent.drain();
                            return;
                        }
                        if (m == QueueSubscription.ASYNC) {
                            fusionMode = m;
                            queue = qs;
                        }
    
                    }
    
                    s.request(bufferSize);
                }
            }
}
