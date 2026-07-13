public class ClientChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
            private int bytesRead;

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                ByteBuf buf = (ByteBuf) msg;
                bytesRead += buf.readableBytes();
                buf.release();
            }

            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
                if (evt == ChannelInputShutdownEvent.INSTANCE && allowHalfClosed) {
                    clientHalfClosedLatch.countDown();
                } else if (evt == ChannelInputShutdownReadComplete.INSTANCE) {
                    ctx.close();
                }
            }

            @Override
            public void channelInactive(ChannelHandlerContext ctx) {
                if (!allowHalfClosed) {
                    clientHalfClosedLatch.countDown();
                }
            }

            @Override
            public void channelReadComplete(ChannelHandlerContext ctx) {
                clientReadCompletes.incrementAndGet();
                if (bytesRead == totalServerBytesWritten) {
                    clientReadAllDataLatch.countDown();
                }
                if (!autoRead) {
                    ctx.read();
                }
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                ctx.close();
            }
        });
    }
}
