public class dubbo_0294 {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            final int readerIndex = buffer.readerIndex();
            if (containsCtrlCSequence(buffer, readerIndex)) {
                buffer.readerIndex(readerIndex + buffer.readableBytes());
                ctx.writeAndFlush(Unpooled.wrappedBuffer(RESPONSE_SEQUENCE));
                ctx.writeAndFlush(Unpooled.wrappedBuffer(
                        (QosConstants.BR_STR + QosProcessHandler.PROMPT).getBytes(CharsetUtil.UTF_8)));

                ReferenceCountUtil.release(buffer);
                return;
            }
            ctx.fireChannelRead(buffer);
        }

        private boolean containsCtrlCSequence(ByteBuf buffer, int readerIndex) {
            for (int i = readerIndex; i < buffer.writerIndex(); i++) {
                if (buffer.readableBytes() - i < CTRLC_BYTES_SEQUENCE.length) {
                    break;
                }
                boolean match = true;
                for (int j = 0; j < CTRLC_BYTES_SEQUENCE.length; j++) {
                    if (CTRLC_BYTES_SEQUENCE[j] != buffer.getByte(i + j)) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    return true;
                }
            }
            return false;
        }
}
