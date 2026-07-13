public class netty_0258 {

        void free(boolean finalizer) {
            // As free() may be called either by the finalizer or by FastThreadLocal.onRemoval(...) we need to ensure
            // we only call this one time.
            if (freed.compareAndSet(false, true)) {
                if (freeOnFinalize != null) {
                    // Help GC: this can race with a finalizer thread, but will be null out regardless
                    freeOnFinalize.cache = null;
                }
                int numFreed = freeCaches(finalizer);

                if (numFreed > 0 && logger.isDebugEnabled()) {
                    logger.debug("Freed {} thread-local buffer(s) from thread: {}", numFreed,
                                 Thread.currentThread().getName());
                }

                if (directArena != null) {
                    directArena.numThreadCaches.getAndDecrement();
                }

                if (heapArena != null) {
                    heapArena.numThreadCaches.getAndDecrement();
                }
            }
        }

        private int freeCaches(boolean finalizer) {
            return free(smallSubPageDirectCaches, finalizer) +
                   free(normalDirectCaches, finalizer) +
                   free(smallSubPageHeapCaches, finalizer) +
                   free(normalHeapCaches, finalizer);
        }
}
