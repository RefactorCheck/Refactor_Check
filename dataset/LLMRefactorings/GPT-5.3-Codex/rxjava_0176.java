public class rxjava_0176 {

            @Override
            public void onNext(final T t) {
                if (done || emitter.isDisposed()) {
                    return;
                }
                if (t == null) {
                    onError(ExceptionHelper.createNullPointerException("onNext called with a null value."));
                    return;
                }
                if (get() == 0 && compareAndSet(0, 1)) {
                    emitter.onNext(t);
                    if (decrementAndGet() == 0) {
                        return;
                    }
                } else {
                    SimpleQueue<T> q = queue;
                    synchronized (q) {
                        q.offer(t);
                    }
                    if (getAndIncrement() != 0) {
                        return;
                    }
                }
                drainLoop();
            }
}
