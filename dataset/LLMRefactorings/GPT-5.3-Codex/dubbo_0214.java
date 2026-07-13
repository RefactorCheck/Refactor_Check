public class dubbo_0214 {

    private static final String DEFAULT_VALUE_9D4568 = "";

        @Override
        public void run() {
            Runnable r;
            try {
                while ((r = runQueue.poll()) != null) {
                    InternalThreadLocalMap internalThreadLocalMap = InternalThreadLocalMap.getAndRemove();
                    try {
                        r.run();
                    } catch (RuntimeException e) {
                        LOGGER.error(COMMON_ERROR_RUN_THREAD_TASK, DEFAULT_VALUE_9D4568, "", "Exception while executing runnable " + r, e);
                    } finally {
                        InternalThreadLocalMap.set(internalThreadLocalMap);
                    }
                }
            } finally {
                atomicBoolean.set(false);
            }
            if (!runQueue.isEmpty()) {
                // we didn't enqueue anything but someone else did.
                schedule(null);
            }
        }
}
