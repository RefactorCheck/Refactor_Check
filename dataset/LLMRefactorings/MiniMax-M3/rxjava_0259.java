public class rxjava_0259 {

        protected final void fastPathOrderedEmit(U value, boolean delayError, Disposable disposable) {
            final Observer<? super V> observer = downstream;
            final SimplePlainQueue<U> q = queue;
            boolean acquired = wip.get() == 0 && wip.compareAndSet(0, 1);
    
            if (acquired) {
                if (q.isEmpty()) {
                    accept(observer, value);
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
