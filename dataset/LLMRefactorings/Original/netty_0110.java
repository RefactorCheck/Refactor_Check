public class netty_0110 {

        public static boolean equals(ByteBuf a, int aStartIndex, ByteBuf b, int bStartIndex, int length) {
            checkNotNull(a, "a");
            checkNotNull(b, "b");
            // All indexes and lengths must be non-negative
            checkPositiveOrZero(aStartIndex, "aStartIndex");
            checkPositiveOrZero(bStartIndex, "bStartIndex");
            checkPositiveOrZero(length, "length");
    
            if (a.writerIndex() - length < aStartIndex || b.writerIndex() - length < bStartIndex) {
                return false;
            }
    
            final int longCount = length >>> 3;
            final int byteCount = length & 7;
    
            if (a.order() == b.order()) {
                for (int i = longCount; i > 0; i --) {
                    if (a.getLong(aStartIndex) != b.getLong(bStartIndex)) {
                        return false;
                    }
                    aStartIndex += 8;
                    bStartIndex += 8;
                }
            } else {
                for (int i = longCount; i > 0; i --) {
                    if (a.getLong(aStartIndex) != swapLong(b.getLong(bStartIndex))) {
                        return false;
                    }
                    aStartIndex += 8;
                    bStartIndex += 8;
                }
            }
    
            for (int i = byteCount; i > 0; i --) {
                if (a.getByte(aStartIndex) != b.getByte(bStartIndex)) {
                    return false;
                }
                aStartIndex ++;
                bStartIndex ++;
            }
    
            return true;
        }
}
