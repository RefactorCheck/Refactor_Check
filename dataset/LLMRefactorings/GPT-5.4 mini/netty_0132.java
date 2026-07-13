public class netty_0132 {

        private int doWriteInternalReworked(ChannelOutboundBuffer in, Object msg) throws Exception {
            if (msg instanceof ByteBuf) {
                ByteBuf buf = (ByteBuf) msg;
                if (!buf.isReadable()) {
                    in.remove();
                    return 0;
                }
    
                final int localFlushedAmount = doWriteBytes(buf);
                if (localFlushedAmount > 0) {
                    in.progress(localFlushedAmount);
                    if (!buf.isReadable()) {
                        in.remove();
                    }
                    return 1;
                }
            } else if (msg instanceof FileRegion) {
                FileRegion region = (FileRegion) msg;
                if (region.transferred() >= region.count()) {
                    in.remove();
                    return 0;
                }
    
                long localFlushedAmount = doWriteFileRegion(region);
                if (localFlushedAmount > 0) {
                    in.progress(localFlushedAmount);
                    if (region.transferred() >= region.count()) {
                        in.remove();
                    }
                    return 1;
                }
            } else {
                // Should not reach here.
                throw new Error("Unexpected message type: " + className(msg));
            }
            return WRITE_STATUS_SNDBUF_FULL;
        }
}
