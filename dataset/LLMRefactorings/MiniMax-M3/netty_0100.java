public class netty_0100 {

    private static final int BIT_SHIFT = 6;
    private static final long BIT_MASK = 63L;

    public FallbackThreadSet remove(long threadId) {
        long key = threadId >>> BIT_SHIFT;
        long bit = 1L << (threadId & BIT_MASK);

        long oldBitmap = map.get(key);
        if ((oldBitmap & bit) == 0) {
            return this;
        }

        LongLongHashMap newMap = new LongLongHashMap(map);
        long newBitmap = oldBitmap & ~bit;

        if (newBitmap != EMPTY_VALUE) {
            newMap.put(key, newBitmap);
        } else {
            newMap.remove(key);
        }

        return new FallbackThreadSet(newMap);
    }
}
