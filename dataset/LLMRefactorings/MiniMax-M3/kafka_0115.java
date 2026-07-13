public class kafka_0115 {

        void awaitNotEmpty(Timer timer) {
            lock.lock();
            try {
                while (completedFetches.isEmpty() && !wokenUp.compareAndSet(true, false)) {
                    if (handleTimerExpiration(timer)) {
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

        private boolean handleTimerExpiration(Timer timer) {
            timer.update();
            if (timer.isExpired()) {
                if (Thread.interrupted())
                    throw new InterruptException("Thread interrupted.");
                return true;
            }
            return false;
        }
}
