public class rxjava_0116 {

    SlotPair<A> addValue(A value) {
        for (;;) {
            SlotPair<A> curr = current.get();

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

            assignValueToSlot(curr, c, value);

            if (curr.releaseSlot()) {
                current.compareAndSet(curr, null);
                return curr;
            }
            return null;
        }
    }

    private void assignValueToSlot(SlotPair<A> pair, int slotIndex, A value) {
        if (slotIndex == 0) {
            pair.first = value;
        } else {
            pair.second = value;
        }
    }
}
