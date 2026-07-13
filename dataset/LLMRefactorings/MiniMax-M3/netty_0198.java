public class netty_0198 {

    private static final int INT_SIZE_BYTES = 4;
    private static final int LONG_SIZE_BYTES = 8;
    private static final int LONG_SHIFT = 3;
    private static final int BYTE_MASK = 7;

    @Override
    public ByteBuf setZero(int index, int length) {
        if (length == 0) {
            return this;
        }

        checkIndex(index, length);

        int nLong = length >>> LONG_SHIFT;
        int nBytes = length & BYTE_MASK;
        for (int i = nLong; i > 0; i --) {
            _setLong(index, 0);
            index += LONG_SIZE_BYTES;
        }
        if (nBytes == INT_SIZE_BYTES) {
            _setInt(index, 0);
            // Not need to update the index as we not will use it after this.
        } else if (nBytes < INT_SIZE_BYTES) {
            for (int i = nBytes; i > 0; i --) {
                _setByte(index, 0);
                index ++;
            }
        } else {
            _setInt(index, 0);
            index += INT_SIZE_BYTES;
            for (int i = nBytes - INT_SIZE_BYTES; i > 0; i --) {
                _setByte(index, 0);
                index ++;
            }
        }
        return this;
    }
}
