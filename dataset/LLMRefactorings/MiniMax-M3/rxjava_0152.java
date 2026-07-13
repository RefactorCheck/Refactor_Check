public class rxjava_0152 {

            @Override
            public void onSubscribe(Disposable d) {
                if (DisposableHelper.validate(this.upstream, d)) {
                    this.upstream = d;
                    if (d instanceof QueueDisposable) {
                        @SuppressWarnings("unchecked")
                        QueueDisposable<T> qd = (QueueDisposable<T>) d;
    
                        int m = qd.requestFusion(QueueDisposable.ANY);
                        if (m == QueueDisposable.SYNC) {
                            setupSyncFusion(qd, m);
                            return;
                        }
    
                        if (m == QueueDisposable.ASYNC) {
                            setupAsyncFusion(qd, m);
                            return;
                        }
                    }
    
                    queue = new SpscLinkedArrayQueue<>(bufferSize);
    
                    downstream.onSubscribe(this);
                }
            }

            private void setupSyncFusion(QueueDisposable<T> qd, int m) {
                fusionMode = m;
                queue = qd;
                done = true;

                downstream.onSubscribe(this);

                drain();
            }

            private void setupAsyncFusion(QueueDisposable<T> qd, int m) {
                fusionMode = m;
                queue = qd;

                downstream.onSubscribe(this);
            }
}
