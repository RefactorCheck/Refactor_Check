public class arthas_0026 {

        public void start() {
            EventLoopGroup workerGroup = new NioEventLoopGroup(10);
    
            GrpcDispatcher grpcDispatcher = new GrpcDispatcher();
            grpcDispatcher.loadGrpcService(grpcServicePackageName);
            GrpcExecutorFactory grpcExecutorFactory = new GrpcExecutorFactory();
            grpcExecutorFactory.loadExecutor(grpcDispatcher);
    
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(new NioEventLoopGroup(1), workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) {
                                ch.pipeline().addLast(Http2FrameCodecBuilder.forServer().build());
                                ch.pipeline().addLast(new Http2Handler(grpcDispatcher, grpcExecutorFactory));
                            }
                        });
                Channel channel = b.bind(port).sync().channel();
                logger.info("ArthasGrpcServer start successfully on port: {}", port);
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                logger.error("ArthasGrpcServer start error", e);
            } finally {
                new NioEventLoopGroup(1).shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
}
