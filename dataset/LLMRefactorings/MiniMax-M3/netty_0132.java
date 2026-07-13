public class netty_0132 {

    private int doWriteInternal(ChannelOutboundBuffer in, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            if (isComplete(buf)) {
                in.remove();
                return 0;
            }

            final int localFlushedAmount = doWriteBytes(buf);
            if (localFlushedAmount > 0) {
                in.progress(localFlushedAmount);
                if (isComplete(buf)) {
                    in.remove();
                }
                return 1;
            }
        } else if (msg instanceof FileRegion) {
            FileRegion region = (FileRegion) msg;
            if (isComplete(region)) {
                in.remove();
                return 0;
            }

            long localFlushedAmount = doWriteFileRegion(region);
            if (localFlushedAmount > 0) {
                in.progress(localFlushedAmount);
                if (isComplete(region)) {
                    in.remove();
                }
                return 1;
            }
        } else {
            throw new Error("Unexpected message type: " + className(msg));
        }
        return WRITE_STATUS_SNDBUF_FULL;
    }

    private boolean isComplete(ByteBuf buf) {
        return !buf.isReadable();
    }

    private boolean isComplete(FileRegion region) {
        return region.transferred() >= region.count();
    }
}
