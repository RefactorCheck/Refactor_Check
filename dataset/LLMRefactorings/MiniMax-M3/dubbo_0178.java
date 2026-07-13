public class dubbo_0178 {

    void expireTimeouts(long deadline) {
        HashedWheelTimeout timeout = head;
        while (timeout != null) {
            timeout = processTimeout(timeout, deadline);
        }
    }

    private HashedWheelTimeout processTimeout(HashedWheelTimeout timeout, long deadline) {
        HashedWheelTimeout next = timeout.next;
        if (timeout.remainingRounds <= 0) {
            next = remove(timeout);
            if (timeout.deadline <= deadline) {
                timeout.expire();
            } else {
                throw new IllegalStateException(String.format(
                    "timeout.deadline (%d) > deadline (%d)", timeout.deadline, deadline));
            }
        } else if (timeout.isCancelled()) {
            next = remove(timeout);
        } else {
            timeout.remainingRounds--;
        }
        return next;
    }
}
