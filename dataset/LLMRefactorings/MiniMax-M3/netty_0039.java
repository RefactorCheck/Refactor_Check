public class netty_0039 {

    private static final int SO_LINGER_ZERO = 0;

    public void testSoLingerZeroCausesOnlyRstOnClose(ServerBootstrap sb, Bootstrap cb) throws Throwable {
        final AtomicReference<Channel> serverChannelRef = new AtomicReference<Channel>();
        final AtomicReference<Throwable> throwableRef = new AtomicReference<Throwable>();
        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        sb.childOption(ChannelOption.SO_LINGER, SO_LINGER_ZERO);
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
        latch.await();
        serverChannelRef.get().close();
        latch2.await();
        Throwable cause = throwableRef.get();
        assertTrue(cause instanceof IOException,
            "actual [type, message]: [" + cause.getClass() + ", " + cause.getMessage() + "]");
        assertRstOnCloseException((IOException) cause, cc);
    }
}
