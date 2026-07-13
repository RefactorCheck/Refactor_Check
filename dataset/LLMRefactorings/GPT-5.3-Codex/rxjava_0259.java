public class rxjava_0259 {

        private void fastPathOrderedEmitCore(U value, boolean delayError, Disposable disposable) {
            accept(observer, value);
        }

        protected final void fastPathOrderedEmit(U value, boolean delayError, Disposable disposable) {
            final Observer<? super V> observer = downstream;
            final SimplePlainQueue<U> q = queue;
    
            if (wip.get() == 0 && wip.compareAndSet(0, 1)) {
                if (q.isEmpty()) {
            fastPathOrderedEmitCore(value, delayError, disposable);
                    if (leave(-1) == 0) {
                        return;
                    }
                } else {
                    q.offer(value);
                }
            } else {
                q.offer(value);
                if (!enter()) {
                    return;
                }
            }
            QueueDrainHelper.drainLoop(q, observer, delayError, disposable, this);
        }
}
