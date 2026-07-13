public class netty_0100 {

            public FallbackThreadSet removeReworked(long threadId) {
                long key = threadId >>> 6;
                long bit = 1L << (threadId & 63);
    
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
