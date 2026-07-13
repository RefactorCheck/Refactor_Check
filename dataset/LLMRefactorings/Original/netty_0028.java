public class netty_0028 {

        public void testReadPendingIsResetAfterEachRead(ServerBootstrap sb, Bootstrap cb) throws Throwable {
            Channel serverChannel = null;
            Channel clientChannel = null;
            try {
                MyInitializer serverInitializer = new MyInitializer();
                sb.option(ChannelOption.SO_BACKLOG, 1024);
                sb.childHandler(serverInitializer);
    
                serverChannel = sb.bind().syncUninterruptibly().channel();
    
                cb.handler(new MyInitializer());
                clientChannel = cb.connect(serverChannel.localAddress()).syncUninterruptibly().channel();
    
                clientChannel.writeAndFlush(randomBufferType(clientChannel.alloc(), new byte[1024], 0, 1024));
    
                // We expect to get 2 exceptions (1 from BuggyChannelHandler and 1 from ExceptionHandler).
                assertTrue(serverInitializer.exceptionHandler.latch1.await(5, TimeUnit.SECONDS));
    
                // After we get the first exception, we should get no more, this is expected to timeout.
                assertFalse(serverInitializer.exceptionHandler.latch2.await(1, TimeUnit.SECONDS),
                    "Encountered " + serverInitializer.exceptionHandler.count.get() +
                                            " exceptions when 1 was expected");
            } finally {
                if (serverChannel != null) {
                    serverChannel.close().syncUninterruptibly();
                }
                if (clientChannel != null) {
                    clientChannel.close().syncUninterruptibly();
                }
            }
        }
}
