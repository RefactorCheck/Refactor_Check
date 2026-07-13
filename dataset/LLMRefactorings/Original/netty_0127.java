public class netty_0127 {

                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            private int totalRead;
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) {
                                ctx.writeAndFlush(ctx.alloc().buffer(1).writeByte(0));
                            }
    
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                if (msg instanceof ByteBuf) {
                                    totalRead += ((ByteBuf) msg).readableBytes();
                                    if (totalRead == expectedBytes) {
                                        latch.countDown();
                                    }
                                }
                                ReferenceCountUtil.release(msg);
                            }
                        });
                    }
}
