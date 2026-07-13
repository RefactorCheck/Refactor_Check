public class rxjava_0048 {

        @Override
        public final void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(upstream, d)) {
                upstream = d;
                if (d instanceof QueueDisposable) {
                    @SuppressWarnings("unchecked")
                    QueueDisposable<T> qd = (QueueDisposable<T>)d;
                    if (handleFusion(qd)) {
                        return;
                    }
                }
                queue = new SpscLinkedArrayQueue<>(prefetch);
                onSubscribeDownstream();
            }
        }

        private boolean handleFusion(QueueDisposable<T> qd) {
            int mode = qd.requestFusion(QueueFuseable.ANY | QueueFuseable.BOUNDARY);
            if (mode == QueueFuseable.SYNC) {
                queue = qd;
                done = true;
                onSubscribeDownstream();
                drain();
                return true;
            }
            else if (mode == QueueFuseable.ASYNC) {
                queue = qd;
                onSubscribeDownstream();
                return true;
            }
            return false;
        }
}
