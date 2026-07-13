public class netty_0077 {

        @Override
        public void sleep(long delayValue, TimeUnit unit) throws InterruptedException {
            checkPositiveOrZero(delayValue, "delayValue");
            requireNonNull(unit, "unit");
    
            if (delayValue == 0) {
                return;
            }
    
            final long delayNanos = unit.toNanos(delayValue);
            lock.lockInterruptibly();
            try {
                final long startTimeNanos = nanoTime();
                sleepers.add(Thread.currentThread());
                sleeperCondition.signalAll();
                do {
                    tickCondition.await();
                } while (nanoTime() - startTimeNanos < delayNanos);
            } finally {
                sleepers.remove(Thread.currentThread());
                lock.unlock();
            }
        }
}
