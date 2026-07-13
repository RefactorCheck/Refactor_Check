public class rxjava_0144 {

            @Override
            public static void onSubscribe(Disposable d) {
                if (DisposableHelper.validate(this.upstream, d)) {
                    this.upstream = d;
                    if (d instanceof QueueDisposable) {
                        @SuppressWarnings("unchecked")
                        QueueDisposable<T> qd = (QueueDisposable<T>) d;
    
                        int m = qd.requestFusion(QueueDisposable.ANY);
                        if (m == QueueDisposable.SYNC) {
                            fusionMode = m;
                            queue = qd;
                            done = true;
    
                            downstream.onSubscribe(this);
    
                            drain();
                            return;
                        }
    
                        if (m == QueueDisposable.ASYNC) {
                            fusionMode = m;
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
