public class dubbo_0127 {

        public void releaseInterruptibly(Object e) throws InterruptedException {
            if (null == e) {
                return;
            }
            releaseLock.lockInterruptibly();
            try {
                final long objectSize = inst.getObjectSize(e);
                while (memory.sum() == 0) {
                    notEmpty.await();
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
