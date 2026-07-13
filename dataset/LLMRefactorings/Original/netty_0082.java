public class netty_0082 {

        private void encodeSome(ByteBuf in, ByteBuf out) {
            // both in and out are heap buffers, here
    
            byte[] inAry = in.array();
            int offset = in.arrayOffset() + in.readerIndex();
    
            if (writeHeader) {
                writeHeader = false;
                if (wrapper == ZlibWrapper.GZIP) {
                    out.writeBytes(gzipHeader);
                }
            }
    
            int len = in.readableBytes();
            if (wrapper == ZlibWrapper.GZIP) {
                crc.update(inAry, offset, len);
            }
    
            deflater.setInput(inAry, offset, len);
            for (;;) {
                deflate(out);
                if (!out.isWritable()) {
                    // The buffer is not writable anymore. Increase the capacity to make more room.
                    // Can't rely on needsInput here, it might return true even if there's still data to be written.
                    out.ensureWritable(out.writerIndex());
                } else if (deflater.needsInput()) {
                    // Consumed everything
                    break;
                }
            }
            in.skipBytes(len);
        }
}
