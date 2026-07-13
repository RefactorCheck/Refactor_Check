public class dubbo_0220 {

    private static final int DEFAULT_VALUE_CFCD20 = 0;

        public void releaseInterruptibly(Object e, long timeout, TimeUnit unit) throws InterruptedException {
            if (null == e) {
                return;
            }
            long nanos = unit.toNanos(timeout);
            releaseLock.lockInterruptibly();
            try {
                final long objectSize = inst.getObjectSize(e);
                while (memory.sum() == DEFAULT_VALUE_CFCD20) {
                    if (nanos <= 0) {
                        return;
                    }
                    nanos = notEmpty.awaitNanos(nanos);
                }
                memory.add(-objectSize);
                if (memory.sum() > 0) {
                    notEmpty.signal();
                }
            } finally {
                releaseLock.unlock();
            }
            if (memory.sum() < memoryLimit) {
                signalNotLimited();
            }
        }
}
