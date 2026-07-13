public class rxjava_0221 {

            SlotPair<T> addValue(T value) {
                for (;;) {
                    SlotPair<T> curr = current.get();
    
                    if (curr == null) {
                        curr = new SlotPair<>();
                        if (!current.compareAndSet(null, curr)) {
                            continue;
                        }
                    }
    
                    int c = curr.tryAcquireSlot();
                    if (c < 0) {
                        current.compareAndSet(curr, null);
                        continue;
                    }
                    if (c == 0) {
                        curr.first = value;
                    } else {
                        curr.second = value;
                    }
    
                    if (curr.releaseSlot()) {
                        current.compareAndSet(curr, null);
                        return curr;
                    }
                    return null;
                }
            }
}
