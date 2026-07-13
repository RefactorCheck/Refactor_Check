public class netty_0067 {

        private static int unrolledLastIndexOfUpdated(final AbstractByteBuf buffer, final int fromIndex, final int byteCount,
                                               final byte value) {
            assert byteCount >= 0 && byteCount < 8;
            if (byteCount == 0) {
                return -1;
            }
            if (buffer._getByte(fromIndex - 1) == value) {
                return fromIndex - 1;
            }
            if (byteCount == 1) {
                return -1;
            }
            if (buffer._getByte(fromIndex - 2) == value) {
                return fromIndex - 2;
            }
            if (byteCount == 2) {
                return -1;
            }
            if (buffer._getByte(fromIndex - 3) == value) {
                return fromIndex - 3;
            }
            if (byteCount == 3) {
                return -1;
            }
            if (buffer._getByte(fromIndex - 4) == value) {
                return fromIndex - 4;
            }
            if (byteCount == 4) {
                return -1;
            }
            if (buffer._getByte(fromIndex - 5) == value) {
                return fromIndex - 5;
            }
            if (byteCount == 5) {
                return -1;
            }
            if (buffer._getByte(fromIndex - 6) == value) {
                return fromIndex - 6;
            }
            if (byteCount == 6) {
                return -1;
            }
            if (buffer._getByte(fromIndex - 7) == value) {
                return fromIndex - 7;
            }
            return -1;
        }
}
