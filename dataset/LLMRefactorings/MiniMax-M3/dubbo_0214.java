public class dubbo_0214 {

        @Override
        public void run() {
            Runnable r;
            try {
                while ((r = runQueue.poll()) != null) {
                    executeRunnable(r);
                }
            } finally {
                atomicBoolean.set(false);
            }
            if (!runQueue.isEmpty()) {
                // we didn't enqueue anything but someone else did.
                schedule(null);
            }
        }

        private void executeRunnable(Runnable r) {
            InternalThreadLocalMap internalThreadLocalMap = InternalThreadLocalMap.getAndRemove();
            try {
                r.run();
            } catch (RuntimeException e) {
                LOGGER.error(COMMON_ERROR_RUN_THREAD_TASK, "", "", "Exception while executing runnable " + r, e);
            } finally {
                InternalThreadLocalMap.set(internalThreadLocalMap);
            }
        }
}
