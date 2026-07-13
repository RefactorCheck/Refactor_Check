public class netty_0209 {

        private int decompress(ByteBufAllocator alloc, SpdyHeadersFrame frame) throws Exception {
            ensureBuffer(alloc);
            byte[] out = decompressed.array();
            int off = decompressed.arrayOffset() + decompressed.writerIndex();
            try {
                int numBytes = decompressor.inflate(out, off, decompressed.writableBytes());
                if (numBytes == 0 && decompressor.needsDictionary()) {
                    numBytes = inflateWithDictionary(out, off);
                }
                if (frame != null) {
                    decompressed.writerIndex(decompressed.writerIndex() + numBytes);
                    decodeHeaderBlock(decompressed, frame);
                    decompressed.discardReadBytes();
                }
    
                return numBytes;
            } catch (DataFormatException e) {
                throw new SpdyProtocolException("Received invalid header block", e);
            }
        }

        private int inflateWithDictionary(byte[] out, int off) {
            try {
                decompressor.setDictionary(SPDY_DICT);
            } catch (IllegalArgumentException ignored) {
                throw INVALID_HEADER_BLOCK;
            }
            return decompressor.inflate(out, off, decompressed.writableBytes());
        }
}
