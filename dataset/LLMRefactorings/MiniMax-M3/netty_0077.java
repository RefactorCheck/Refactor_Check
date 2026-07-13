public class netty_0077 {

        @Override
        public void sleep(long delay, TimeUnit unit) throws InterruptedException {
            checkPositiveOrZero(delay, "delay");
            requireNonNull(unit, "unit");
    
            if (delay == 0) {
                return;
            }
    
            final long delayNanos = unit.toNanos(delay);
            lock.lockInterruptibly();
            try {
                sleepers.add(Thread.currentThread());
                sleeperCondition.signalAll();
                awaitDelay(delayNanos);
            } finally {
                sleepers.remove(Thread.currentThread());
                lock.unlock();
            }
        }

        private void awaitDelay(long delayNanos) throws InterruptedException {
            final long startTimeNanos = nanoTime();
            do {
                tickCondition.await();
            } while (nanoTime() - startTimeNanos < delayNanos);
        }
}
