public class netty_0272 {

    private static final int CHANNEL_ACTIVE = 0;
    private static final int CHANNEL_INACTIVE = 1;

    public void testChannelEventsFiredWhenClosedDirectly(ServerBootstrap sb, Bootstrap cb) throws Throwable {
        final BlockingQueue<Integer> events = new LinkedBlockingQueue<Integer>();

        Channel sc = null;
        Channel cc = null;
        try {
            sb.childHandler(new ChannelInboundHandlerAdapter());
            sc = sb.bind().syncUninterruptibly().channel();

            cb.handler(new ChannelInboundHandlerAdapter() {
                @Override
                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                    events.add(CHANNEL_ACTIVE);
                }

                @Override
                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                    events.add(CHANNEL_INACTIVE);
                }
            });
            // Connect and directly close again.
            cc = cb.connect(sc.localAddress()).addListener(ChannelFutureListener.CLOSE).
                    syncUninterruptibly().channel();
            assertEquals(CHANNEL_ACTIVE, events.take().intValue());
            assertEquals(CHANNEL_INACTIVE, events.take().intValue());
        } finally {
            if (cc != null) {
                cc.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    }
}
