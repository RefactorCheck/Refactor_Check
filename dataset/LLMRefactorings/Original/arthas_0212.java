public class arthas_0212 {

        public void start() {
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new GrpcWebProxyServerInitializer(grpcPort));
                channel = serverBootstrap.bind(port).sync().channel();
    
                logger.info("grpc web proxy server started, listening on " + port);
                System.out.println("grpc web proxy server started, listening on " + port);
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
