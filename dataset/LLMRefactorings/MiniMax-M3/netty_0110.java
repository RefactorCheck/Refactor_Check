public class netty_0110 {

    public static boolean equals(ByteBuf a, int aStartIndex, ByteBuf b, int bStartIndex, int length) {
        checkNotNull(a, "a");
        checkNotNull(b, "b");
        checkPositiveOrZero(aStartIndex, "aStartIndex");
        checkPositiveOrZero(bStartIndex, "bStartIndex");
        checkPositiveOrZero(length, "length");

        if (a.writerIndex() - length < aStartIndex || b.writerIndex() - length < bStartIndex) {
            return false;
        }

        final int longCount = length >>> 3;
        final int byteCount = length & 7;

        if (!equalsLongs(a, aStartIndex, b, bStartIndex, longCount, a.order() == b.order())) {
            return false;
        }
        aStartIndex += longCount * 8;
        bStartIndex += longCount * 8;

        for (int i = byteCount; i > 0; i--) {
            if (a.getByte(aStartIndex) != b.getByte(bStartIndex)) {
                return false;
            }
            aStartIndex++;
            bStartIndex++;
        }

        return true;
    }

    private static boolean equalsLongs(ByteBuf a, int aStartIndex, ByteBuf b, int bStartIndex, int longCount, boolean sameOrder) {
        for (int i = longCount; i > 0; i--) {
            long bLong = b.getLong(bStartIndex);
            if (!sameOrder) {
                bLong = swapLong(bLong);
            }
            if (a.getLong(aStartIndex) != bLong) {
                return false;
            }
            aStartIndex += 8;
            bStartIndex += 8;
        }
        return true;
    }
}
