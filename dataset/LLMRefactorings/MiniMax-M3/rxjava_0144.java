public class rxjava_0144 {

    @Override
    public void onSubscribe(Disposable d) {
        if (DisposableHelper.validate(this.upstream, d)) {
            this.upstream = d;
            if (d instanceof QueueDisposable) {
                @SuppressWarnings("unchecked")
                QueueDisposable<T> qd = (QueueDisposable<T>) d;

                int m = qd.requestFusion(QueueDisposable.ANY);
                if (m == QueueDisposable.SYNC) {
                    handleSyncFusion(qd, m);
                    return;
                }

                if (m == QueueDisposable.ASYNC) {
                    handleAsyncFusion(qd, m);
                    return;
                }
            }

            queue = new SpscLinkedArrayQueue<>(bufferSize);

            downstream.onSubscribe(this);
        }
    }

    private void handleSyncFusion(QueueDisposable<T> qd, int m) {
        fusionMode = m;
        queue = qd;
        done = true;
        downstream.onSubscribe(this);
        drain();
    }

    private void handleAsyncFusion(QueueDisposable<T> qd, int m) {
        fusionMode = m;
        queue = qd;
        downstream.onSubscribe(this);
    }
}
