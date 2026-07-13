public class rxjava_0083 {

            @Override
            public void onSubscribe(Subscription s) {
                if (SubscriptionHelper.setOnce(this, s)) {
    
                    if (s instanceof QueueSubscription) {
                        @SuppressWarnings("unchecked")
                        final QueueSubscription<U> extractedQs = (QueueSubscription<U>) s;
                        QueueSubscription<U> qs = extractedQs;
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
