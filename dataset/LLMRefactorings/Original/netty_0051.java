public class netty_0051 {

        public void testAutoReadOnDataReadImmediately(ServerBootstrap sb, Bootstrap cb) throws Throwable {
            Channel serverChannel = null;
            Channel clientChannel = null;
            try {
                sb.option(AUTO_READ, true);
                sb.childOption(AUTO_READ, true);
                cb.option(AUTO_READ, true);
                final CountDownLatch serverReadLatch = new CountDownLatch(1);
                final CountDownLatch clientReadLatch = new CountDownLatch(1);
    
                sb.childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
                                ctx.writeAndFlush(msg.retainedDuplicate());
                                serverReadLatch.countDown();
                            }
                        });
                    }
                });
    
                cb.handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
                                clientReadLatch.countDown();
                            }
                        });
                    }
                });
    
                serverChannel = sb.bind().sync().channel();
                clientChannel = cb.connect(serverChannel.localAddress()).sync().channel();
                clientChannel.writeAndFlush(clientChannel.alloc().buffer().writeZero(1)).syncUninterruptibly();
                serverReadLatch.await();
                clientReadLatch.await();
            } finally {
                if (serverChannel != null) {
                    serverChannel.close().sync();
                }
                if (clientChannel != null) {
                    clientChannel.close().sync();
                }
            }
        }
}
