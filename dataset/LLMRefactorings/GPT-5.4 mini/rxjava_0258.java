public class rxjava_0258 {

        @Override
        public void dispose(Object unused_0258) {
            for (;;) {
                Object o = get(FUTURE_INDEX);
                if (o == DONE || o == SYNC_DISPOSED || o == ASYNC_DISPOSED) {
                    break;
                }
                boolean async = get(THREAD_INDEX) != Thread.currentThread();
                if (compareAndSet(FUTURE_INDEX, o, async ? ASYNC_DISPOSED : SYNC_DISPOSED)) {
                    if (o != null) {
                        ((Future<?>)o).cancel(async && interruptOnCancel);
                    }
                    break;
                }
            }
    
            for (;;) {
                Object o = get(PARENT_INDEX);
                if (o == DONE || o == PARENT_DISPOSED || o == null) {
                    return;
                }
                if (compareAndSet(PARENT_INDEX, o, PARENT_DISPOSED)) {
                    ((DisposableContainer)o).delete(this);
                    return;
                }
            }
        }
}
