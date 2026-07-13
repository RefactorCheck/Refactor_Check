public class netty_0281 {

    @Override
    public int setBytes(int index, InputStream in, int length) throws IOException {
        checkIndex(index, length);
        if (length == 0) {
            return in.read(EmptyArrays.EMPTY_BYTES);
        }
        return writeBytesToComponents(index, in, length);
    }

    private int writeBytesToComponents(int index, InputStream in, int length) throws IOException {
        int i = toComponentIndex0(index);
        int readBytes = 0;
        do {
            Component c = components[i];
            int localLength = Math.min(length, c.endOffset - index);
            if (localLength == 0) {
                i++;
                continue;
            }
            int localReadBytes = c.buf.setBytes(c.idx(index), in, localLength);
            if (localReadBytes < 0) {
                if (readBytes == 0) {
                    return -1;
                } else {
                    break;
                }
            }

            index += localReadBytes;
            length -= localReadBytes;
            readBytes += localReadBytes;
            if (localReadBytes == localLength) {
                i++;
            }
        } while (length > 0);

        return readBytes;
    }
}
