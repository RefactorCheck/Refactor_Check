public class rxjava_0083 {

    @Override
    public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.setOnce(this, s)) {
            if (!handleQueueSubscription(s)) {
                s.request(bufferSize);
            }
        }
    }

    private boolean handleQueueSubscription(Subscription s) {
        if (s instanceof QueueSubscription) {
            @SuppressWarnings("unchecked")
            QueueSubscription<U> qs = (QueueSubscription<U>) s;
            int m = qs.requestFusion(QueueSubscription.ANY | QueueSubscription.BOUNDARY);
            if (m == QueueSubscription.SYNC) {
                fusionMode = m;
                queue = qs;
                done = true;
                parent.drain();
                return true;
            }
            if (m == QueueSubscription.ASYNC) {
                fusionMode = m;
                queue = qs;
            }
        }
        return false;
    }
}
