public class arthas_0212 {

    private static final String SERVER_STARTED_MESSAGE = "grpc web proxy server started, listening on ";

    public void start() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new GrpcWebProxyServerInitializer(grpcPort));
            channel = serverBootstrap.bind(port).sync().channel();

            logger.info(SERVER_STARTED_MESSAGE + port);
            System.out.println(SERVER_STARTED_MESSAGE + port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.info("fail to start grpc web proxy server!");
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
