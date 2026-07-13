public class rxjava_0156 {

        private void onSubscribeCore(Disposable d) {
            parent.innerComplete(this);
        }

        @Override
        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d)) {
                if (d instanceof QueueDisposable) {
                    @SuppressWarnings("unchecked")
                    QueueDisposable<T> qd = (QueueDisposable<T>) d;
    
                    int m = qd.requestFusion(QueueDisposable.ANY);
                    if (m == QueueSubscription.SYNC) {
                        fusionMode = m;
                        queue = qd;
                        done = true;
            onSubscribeCore(d);
                        return;
                    }
                    if (m == QueueDisposable.ASYNC) {
                        fusionMode = m;
                        queue = qd;
                        return;
                    }
                }
    
                queue = QueueDrainHelper.createQueue(-prefetch);
            }
        }
}
