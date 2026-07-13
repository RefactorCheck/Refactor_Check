public class netty_0293 {

        long allocate() {
            if (numAvail == 0 || !doNotDestroy) {
                return -1;
            }
    
            final int bitmapIdx = getNextAvail();
            if (bitmapIdx < 0) {
                removeFromPool(); // Subpage appear to be in an invalid state. Remove to prevent repeated errors.
                throw new AssertionError("No next available bitmap index found (bitmapIdx = " + bitmapIdx + "), " +
                        "even though there are supposed to be (numAvail = " + numAvail + ") " +
                        "out of (maxNumElems = " + maxNumElems + ") available indexes.");
            }
            int q = bitmapIdx >>> 6;
            int r = bitmapIdx & 63;
            assert (bitmap[q] >>> r & 1) == 0;
            bitmap[q] |= 1L << r;
    
            if (-- numAvail == 0) {
                removeFromPool();
            }
    
            return toHandle(bitmapIdx);
        }
}
