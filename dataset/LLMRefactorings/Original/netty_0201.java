public class netty_0201 {

            private boolean allocateWithoutLock(int size, int maxCapacity, AdaptiveByteBuf buf) {
                Chunk curr = NEXT_IN_LINE.getAndSet(this, null);
                if (curr == MAGAZINE_FREED) {
                    // Allocation raced with a stripe-resize that freed this magazine.
                    restoreMagazineFreed();
                    return false;
                }
                if (curr == null) {
                    curr = group.pollChunk(size);
                    if (curr == null) {
                        return false;
                    }
                    curr.attachToMagazine(this);
                }
                boolean allocated = false;
                int remainingCapacity = curr.remainingCapacity();
                int startingCapacity = chunkController.computeBufferCapacity(
                        size, maxCapacity, true /* never update stats as we don't hold the magazine lock */);
                if (remainingCapacity >= size &&
                        curr.readInitInto(buf, size, Math.min(remainingCapacity, startingCapacity), maxCapacity)) {
                    allocated = true;
                    remainingCapacity = curr.remainingCapacity();
                }
                try {
                    if (remainingCapacity >= RETIRE_CAPACITY) {
                        transferToNextInLineOrRelease(curr);
                        curr = null;
                    }
                } finally {
                    if (curr != null) {
                        curr.releaseFromMagazine();
                    }
                }
                return allocated;
            }
}
