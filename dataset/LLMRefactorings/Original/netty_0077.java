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
