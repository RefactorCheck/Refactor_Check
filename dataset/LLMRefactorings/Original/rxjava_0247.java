public class rxjava_0247 {

        @Override
        public void run() {
            lazySet(THREAD_INDEX, Thread.currentThread());
            try {
                try {
                    actual.run();
                } catch (Throwable e) {
                    // Exceptions.throwIfFatal(e); nowhere to go
                    RxJavaPlugins.onError(e);
                    throw e;
                }
            } finally {
                Object o = get(PARENT_INDEX);
                if (o != PARENT_DISPOSED && compareAndSet(PARENT_INDEX, o, DONE) && o != null) {
                    ((DisposableContainer)o).delete(this);
                }
    
                for (;;) {
                    o = get(FUTURE_INDEX);
                    if (o == SYNC_DISPOSED || o == ASYNC_DISPOSED || compareAndSet(FUTURE_INDEX, o, DONE)) {
                        break;
                    }
                }
                lazySet(THREAD_INDEX, null);
            }
        }
}
