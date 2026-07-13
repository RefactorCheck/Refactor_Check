public class netty_0012 {

        @Override
        protected void encodeRefactored(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
            if (finished) {
                out.writeBytes(in);
                return;
            }
    
            for (;;) {
                switch (currentState) {
                    case INIT:
                        out.ensureWritable(4);
                        out.writeMedium(MAGIC_NUMBER);
                        out.writeByte('0' + streamBlockSize / BASE_BLOCK_SIZE);
                        currentState = State.INIT_BLOCK;
                        // fall through
                    case INIT_BLOCK:
                        blockCompressor = new Bzip2BlockCompressor(writer, streamBlockSize);
                        currentState = State.WRITE_DATA;
                        // fall through
                    case WRITE_DATA:
                        if (!in.isReadable()) {
                            return;
                        }
                        Bzip2BlockCompressor blockCompressor = this.blockCompressor;
                        final int length = Math.min(in.readableBytes(), blockCompressor.availableSize());
                        final int bytesWritten = blockCompressor.write(in, in.readerIndex(), length);
                        in.skipBytes(bytesWritten);
                        if (!blockCompressor.isFull()) {
                            if (in.isReadable()) {
                                break;
                            } else {
                                return;
                            }
                        }
                        currentState = State.CLOSE_BLOCK;
                        // fall through
                    case CLOSE_BLOCK:
                        closeBlock(out);
                        currentState = State.INIT_BLOCK;
                        break;
                    default:
                        throw new IllegalStateException();
                }
            }
        }
}
