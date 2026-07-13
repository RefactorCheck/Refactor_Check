public class netty_0272 {

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
                        events.add(0);
                    }
    
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        events.add(1);
                    }
                });
                // Connect and directly close again.
                cc = cb.connect(sc.localAddress()).addListener(ChannelFutureListener.CLOSE).
                        syncUninterruptibly().channel();
                assertEquals(0, events.take().intValue());
                assertEquals(1, events.take().intValue());
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
