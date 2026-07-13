public class rxjava_0019 {

            @Override
            public void onSubscribe(final Disposable d) {
                if (DisposableHelper.validate(this.upstream, d)) {
                    this.upstream = d;
    
                    if (d instanceof QueueDisposable) {
                        @SuppressWarnings("unchecked")
                        QueueDisposable<T> qd = (QueueDisposable<T>) d;
    
                        int m = qd.requestFusion(QueueDisposable.ANY);
                        if (m == QueueDisposable.SYNC) {
                            sourceMode = m;
                            queue = qd;
                            done = true;
    
                            downstream.onSubscribe(this);
    
                            drain();
                            return;
                        }
                        if (m == QueueDisposable.ASYNC) {
                            sourceMode = m;
                            queue = qd;
    
                            downstream.onSubscribe(this);
    
                            return;
                        }
                    }
    
                    queue = new SpscLinkedArrayQueue<>(bufferSize);
    
                    downstream.onSubscribe(this);
                }
            }
}
