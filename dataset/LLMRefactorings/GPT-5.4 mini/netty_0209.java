public class netty_0209 {

        private int decompressRenamed(ByteBufAllocator alloc, SpdyHeadersFrame frame) throws Exception {
            ensureBuffer(alloc);
            byte[] out = decompressed.array();
            int off = decompressed.arrayOffset() + decompressed.writerIndex();
            try {
                int numBytes = decompressor.inflate(out, off, decompressed.writableBytes());
                if (numBytes == 0 && decompressor.needsDictionary()) {
                    try {
                        decompressor.setDictionary(SPDY_DICT);
                    } catch (IllegalArgumentException ignored) {
                        throw INVALID_HEADER_BLOCK;
                    }
                    numBytes = decompressor.inflate(out, off, decompressed.writableBytes());
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
}
