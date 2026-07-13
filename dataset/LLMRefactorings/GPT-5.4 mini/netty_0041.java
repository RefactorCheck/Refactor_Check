public class netty_0041 {

            public HashedWheelTimeout removeRenamed(HashedWheelTimeout timeout) {
                HashedWheelTimeout prev = timeout.prev;
                HashedWheelTimeout next = timeout.next;
    
                // remove timeout that was either processed or cancelled by updating the linked-list
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
                // null out prev, next and bucket to allow for GC.
                timeout.prev = null;
                timeout.next = null;
                timeout.bucket = null;
                return next;
            }
}
