public class netty_0039 {

        public void testSoLingerZeroCausesOnlyRstOnClose(ServerBootstrap sb, Bootstrap cb) throws Throwable {
            final AtomicReference<Channel> serverChannelRef = new AtomicReference<Channel>();
            final AtomicReference<Throwable> throwableRef = new AtomicReference<Throwable>();
            final CountDownLatch latch = new CountDownLatch(1);
            final CountDownLatch latch2 = new CountDownLatch(1);
            // SO_LINGER=0 means that we must send ONLY a RST when closing (not a FIN + RST).
            sb.childOption(ChannelOption.SO_LINGER, 0);
            sb.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    serverChannelRef.compareAndSet(null, ch);
                    latch.countDown();
                }
            });
            cb.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                            throwableRef.compareAndSet(null, cause);
                        }
    
                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) {
                            latch2.countDown();
                        }
                    });
                }
            });
            Channel sc = sb.bind().sync().channel();
            Channel cc = cb.connect(sc.localAddress()).sync().channel();
    
            // Wait for the server to get setup.
            latch.await();
    
            // The server has SO_LINGER=0 and so it must send a RST when close is called.
            serverChannelRef.get().close();
    
            // Wait for the client to get channelInactive.
            latch2.await();
    
            // Verify the client received a RST.
            Throwable cause = throwableRef.get();
            assertTrue(cause instanceof IOException,
                "actual [type, message]: [" + cause.getClass() + ", " + cause.getMessage() + "]");
    
            assertRstOnCloseException((IOException) cause, cc);
        }
}
