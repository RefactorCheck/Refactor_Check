public class kafka_0115 {

        void awaitNotEmpty(Timer timer) {
            lock.lock();
            try {
                while (completedFetches.isEmpty() && !wokenUp.compareAndSet(true, false)) {
                    // Update the timer before we head into the loop in case it took a while to get the lock.
                    timer.update();
    
                    if (timer.isExpired()) {
                        if (Thread.interrupted())
                            throw new InterruptException("Thread interrupted.");
                        break;
                    }
    
                    if (!notEmptyCondition.await(timer.remainingMs(), TimeUnit.MILLISECONDS)) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new InterruptException("Timeout waiting for results from fetching records", e);
            } finally {
                lock.unlock();
                timer.update();
            }
        }
}
