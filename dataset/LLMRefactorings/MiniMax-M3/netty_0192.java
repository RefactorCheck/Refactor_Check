public class netty_0192 {

    private int chooseFirstFreeBuddy(int index, int size, int currOffset) {
        byte[] buddies = this.buddies;
        while (index < buddies.length) {
            byte buddy = buddies[index];
            int currValue = MIN_BUDDY_SIZE << (buddy & SHIFT_MASK);
            if (isBuddyUnavailable(buddy, currValue, size)) {
                return -1;
            }
            if (canClaimBuddy(buddy, currValue, size)) {
                buddies[index] |= IS_CLAIMED;
                return currOffset;
            }
            int found = chooseFirstFreeBuddy(index << 1, size, currOffset);
            if (found != -1) {
                buddies[index] |= HAS_CLAIMED_CHILDREN;
                return found;
            }
            index = (index << 1) + 1;
            currOffset += currValue >> 1;
        }
        return -1;
    }

    private boolean isBuddyUnavailable(byte buddy, int currValue, int size) {
        return currValue < size || (buddy & IS_CLAIMED) == IS_CLAIMED;
    }

    private boolean canClaimBuddy(byte buddy, int currValue, int size) {
        return currValue == size && (buddy & HAS_CLAIMED_CHILDREN) == 0;
    }
}
