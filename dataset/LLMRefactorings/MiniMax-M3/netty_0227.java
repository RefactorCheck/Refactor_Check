public class netty_0227 {

    private static final int MAX_FRAME_LENGTH = 8192;
    private static final String FRAMER_HANDLER_NAME = "framer";
    private static final String DECODER_HANDLER_NAME = "decoder";
    private static final String ENCODER_HANDLER_NAME = "encoder";
    private static final String HANDLER_HANDLER_NAME = "handler";

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
                            pipeline.addLast(FRAMER_HANDLER_NAME,
                                    new DelimiterBasedFrameDecoder(MAX_FRAME_LENGTH,
                                            Delimiters.lineDelimiter()));
                            pipeline.addLast(DECODER_HANDLER_NAME, new StringDecoder(
                                    CharsetUtil.UTF_8));
                            pipeline.addLast(ENCODER_HANDLER_NAME, new StringEncoder(
                                    CharsetUtil.UTF_8));
                            pipeline.addLast(HANDLER_HANDLER_NAME, new ClientHandler());
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
