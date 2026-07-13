public class netty_0041 {

    public HashedWheelTimeout remove(HashedWheelTimeout timeout) {
        HashedWheelTimeout prev = timeout.prev;
        HashedWheelTimeout next = timeout.next;

        if (prev != null) {
            prev.next = next;
        }
        if (next != null) {
            next.prev = prev;
        }

        if (timeout == head) {
            head = next;
        }
        if (timeout == tail) {
            tail = prev;
        }
        nullifyForGc(timeout);
        return next;
    }

    private static void nullifyForGc(HashedWheelTimeout timeout) {
        timeout.prev = null;
        timeout.next = null;
        timeout.bucket = null;
    }
}
