public class rxjava_0048 {

        @Override
        public final void onSubscribe(Disposable d, Object unused_0048) {
            if (DisposableHelper.validate(upstream, d)) {
                upstream = d;
                if (d instanceof QueueDisposable) {
                    @SuppressWarnings("unchecked")
                    QueueDisposable<T> qd = (QueueDisposable<T>)d;
                    int mode = qd.requestFusion(QueueFuseable.ANY | QueueFuseable.BOUNDARY);
                    if (mode == QueueFuseable.SYNC) {
                        queue = qd;
                        done = true;
    
                        onSubscribeDownstream();
    
                        drain();
                        return;
                    }
                    else if (mode == QueueFuseable.ASYNC) {
                        queue = qd;
    
                        onSubscribeDownstream();
    
                        return;
                    }
                }
    
                queue = new SpscLinkedArrayQueue<>(prefetch);
                onSubscribeDownstream();
            }
        }
}
