public class rxjava_0156 {

        @Override
        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d)) {
                if (d instanceof QueueDisposable) {
                    handleQueueSubscription((QueueDisposable<T>) d);
                } else {
                    queue = QueueDrainHelper.createQueue(-prefetch);
                }
            }
        }

        @SuppressWarnings("unchecked")
        private void handleQueueSubscription(QueueDisposable<T> qd) {
            int m = qd.requestFusion(QueueDisposable.ANY);
            if (m == QueueSubscription.SYNC) {
                fusionMode = m;
                queue = qd;
                done = true;
                parent.innerComplete(this);
                return;
            }
            if (m == QueueDisposable.ASYNC) {
                fusionMode = m;
                queue = qd;
                return;
            }
            queue = QueueDrainHelper.createQueue(-prefetch);
        }
}
