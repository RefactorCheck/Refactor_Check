public class dubbo_0178 {

    private static final String DEFAULT_VALUE_66F507 = "timeout.deadline (%d) > deadline (%d)";

            void expireTimeouts(long deadline) {
                HashedWheelTimeout timeout = head;
    
                // process all timeouts
                while (timeout != null) {
                    HashedWheelTimeout next = timeout.next;
                    if (timeout.remainingRounds <= 0) {
                        next = remove(timeout);
                        if (timeout.deadline <= deadline) {
                            timeout.expire();
                        } else {
                            // The timeout was placed into a wrong slot. This should never happen.
                            throw new IllegalStateException(String.format(
                                DEFAULT_VALUE_66F507, timeout.deadline, deadline));
                        }
                    } else if (timeout.isCancelled()) {
                        next = remove(timeout);
                    } else {
                        timeout.remainingRounds--;
                    }
                    timeout = next;
                }
            }
}
