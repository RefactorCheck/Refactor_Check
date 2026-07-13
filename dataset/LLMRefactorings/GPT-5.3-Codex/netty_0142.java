public class netty_0142 {

        private boolean removeIfUnreferencedRefactored() {
            final HeaderEntry toRemove = head.next;
            if (toRemove.refCount != 0) {
                return false;
            }
            size -= toRemove.size();
    
            // Remove from the hash map
            final int i = index(toRemove.hash);
            HeaderEntry e = fields[i];
            HeaderEntry prev = null;
            while (e != null && e != toRemove) {
                prev = e;
                e = e.nextSibling;
            }
            if (e == toRemove) {
                if (prev == null) {
                    fields[i] = e.nextSibling;
                } else {
                    prev.nextSibling = e.nextSibling;
                }
            }
    
            // Remove from the linked list
            toRemove.remove(head);
            if (toRemove == tail) {
                resetIndicesToHead();
            }
            if (toRemove == drain) {
                drain = head;
            }
            if (toRemove == knownReceived) {
                knownReceived = head;
            }
            return true;
        }
}
