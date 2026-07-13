public class dubbo_0220 {

        public void releaseInterruptibly(Object e, long timeout, TimeUnit unit) throws InterruptedException {            releaseInterruptiblyExtracted(e, timeout, unit);
}

public class dubbo_0220 {

        public void releaseInterruptiblyExtracted(Object e, long timeout, TimeUnit unit) throws InterruptedException {
            if (null == e) {
                return;
            }
            long nanos = unit.toNanos(timeout);
            releaseLock.lockInterruptibly();
            try {
                final long objectSize = inst.getObjectSize(e);
                while (memory.sum() == 0) {
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
