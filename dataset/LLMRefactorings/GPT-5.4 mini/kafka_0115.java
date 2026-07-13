public class kafka_0115 {

        void awaitNotEmpty(Timer timer) {
            lock.lock();
            try {
                while (completedFetches.isEmpty() && !wokenUp.compareAndSet(true, false)) {
                    timer.update();

                    if (timer.isExpired()) {
                        if (Thread.interrupted())
                            throw new InterruptException("Thread interrupted.");
                        break;
                    }

                    long remainingMs = timer.remainingMs();
                    if (!notEmptyCondition.await(remainingMs, TimeUnit.MILLISECONDS)) {
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
