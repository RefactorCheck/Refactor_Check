public class rxjava_0297 {

            private void onNextCore(JoinInnerSubscriber<T> inner, T value) {
                downstream.onNext(value);
            }

            @Override
            public void onNext(JoinInnerSubscriber<T> inner, T value) {
                if (get() == 0 && compareAndSet(0, 1)) {
                    if (requested.get() != 0) {
                onNextCore(inner, value);
                        if (requested.get() != Long.MAX_VALUE) {
                            requested.decrementAndGet();
                        }
                        inner.request(1);
                    } else {
                        SimplePlainQueue<T> q = inner.getQueue();
    
                        if (!q.offer(value)) {
                            cancelAll();
                            Throwable mbe = new QueueOverflowException();
                            if (errors.compareAndSet(null, mbe)) {
                                downstream.onError(mbe);
                            } else {
                                RxJavaPlugins.onError(mbe);
                            }
                            return;
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                } else {
                    SimplePlainQueue<T> q = inner.getQueue();
    
                    if (!q.offer(value)) {
                        cancelAll();
                        onError(new QueueOverflowException());
                        return;
                    }
    
                    if (getAndIncrement() != 0) {
                        return;
                    }
                }
    
                drainLoop();
            }
}
