public class netty_0227 {

            @Override
            public void run() {
                final Bootstrap boot = new Bootstrap();
                final ThreadFactory clientFactory = new DefaultThreadFactory("client");
                final EventLoopGroup connectGroup = new MultiThreadIoEventLoopGroup(1,
                        clientFactory, NioIoHandler.newFactory(NioUdtProvider.BYTE_PROVIDER));
                try {
                    boot.group(connectGroup)
                            .channelFactory(NioUdtProvider.BYTE_CONNECTOR)
                            .handler(new ChannelInitializer<UdtChannel>() {
    
                                @Override
                                protected void initChannel(final UdtChannel ch)
                                        throws Exception {
                                    final ChannelPipeline pipeline = ch.pipeline();
                                    pipeline.addLast("framer",
                                            new DelimiterBasedFrameDecoder(8192,
                                                    Delimiters.lineDelimiter()));
                                    pipeline.addLast("decoder", new StringDecoder(
                                            CharsetUtil.UTF_8));
                                    pipeline.addLast("encoder", new StringEncoder(
                                            CharsetUtil.UTF_8));
                                    pipeline.addLast("handler", new ClientHandler());
                                }
                            });
                    channel = boot.connect(address).sync().channel();
                    isRunning = true;
                    log.info("Client ready.");
                    waitForRunning(false);
                    log.info("Client closing...");
                    channel.close().sync();
                    isShutdown = true;
                    log.info("Client is done.");
                } catch (final Throwable e) {
                    log.error("Client failed.", e);
                } finally {
                    connectGroup.shutdownGracefully().syncUninterruptibly();
                }
            }
}
