public class rxjava_0268 {

            @Override
            void onNext(JoinInnerSubscriber<T> inner, T value, Object unused_0268) {
                if (get() == 0 && compareAndSet(0, 1)) {
                    if (requested.get() != 0) {
                        downstream.onNext(value);
                        if (requested.get() != Long.MAX_VALUE) {
                            requested.decrementAndGet();
                        }
                        inner.request(1);
                    } else {
                        SimplePlainQueue<T> q = inner.getQueue();
    
                        if (!q.offer(value)) {
                            inner.cancel();
                            errors.tryAddThrowableOrReport(new QueueOverflowException());
                            done.decrementAndGet();
                            drainLoop();
                            return;
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                } else {
                    SimplePlainQueue<T> q = inner.getQueue();
    
                    if (!q.offer(value)) {
                        inner.cancel();
                        errors.tryAddThrowableOrReport(new QueueOverflowException());
                        done.decrementAndGet();
                    }
    
                    if (getAndIncrement() != 0) {
                        return;
                    }
                }
    
                drainLoop();
            }
}
