public class kafka_0244 {

        @Override
        public void waitObject(Object obj, Supplier<Boolean> condition, long deadlineMs) throws InterruptedException {
            Listener listener = () -> {
                synchronized (obj) {
                    obj.notify();
                }
            };
            listeners.add(listener);
            try {
                synchronized (obj) {
                    while (milliseconds() < deadlineMs && !condition.get()) {
                        obj.wait();
                    }
                    boolean conditionMet = condition.get();
                    if (!conditionMet)
                        throw new TimeoutException("Condition not satisfied before deadline");
                }
            } finally {
                listeners.remove(listener);
            }
        }
}
