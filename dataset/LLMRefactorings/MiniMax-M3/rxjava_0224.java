public class rxjava_0224 {

    void innerValue(int index, T value) {

        boolean replenishInsteadOfDrain = updateLatest(index, value);

        if (replenishInsteadOfDrain) {
            subscribers[index].requestOne();
        } else {
            drain();
        }
    }

    private boolean updateLatest(int index, T value) {

        synchronized (this) {
            Object[] os = latest;

            int localNonEmptySources = nonEmptySources;

            if (os[index] == null) {
                localNonEmptySources++;
                nonEmptySources = localNonEmptySources;
            }

            os[index] = value;

            if (os.length == localNonEmptySources) {

                queue.offer(subscribers[index], os.clone());

                return false;
            } else {
                return true;
            }
        }
    }
}
