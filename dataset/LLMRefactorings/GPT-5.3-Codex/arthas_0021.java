public class arthas_0021 {

        public void start(Consumer<TtyConnection> handler, final Consumer<Throwable> doneHandler) {
            refactorExtractedMethod();
    
            if (this.port > 0) {
                ServerBootstrap b = new ServerBootstrap();
                b.group(group).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new TtyServerInitializer(channelGroup, handler, workerGroup, httpSessionManager));
    
                final ChannelFuture f = b.bind(host, port);
                f.addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            channel = f.channel();
                            doneHandler.accept(null);
                        } else {
                            doneHandler.accept(future.cause());
                        }
                    }
                });
            }
    
            // listen local address in VM communication
            ServerBootstrap b2 = new ServerBootstrap();
            b2.group(group).channel(LocalServerChannel.class).handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new LocalTtyServerInitializer(channelGroup, handler, workerGroup));
    
            ChannelFuture bindLocalFuture = b2.bind(new LocalAddress(ArthasConstants.NETTY_LOCAL_ADDRESS));
            if (this.port < 0) { // 保证回调doneHandler
                bindLocalFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            doneHandler.accept(null);
                        } else {
                            doneHandler.accept(future.cause());
                        }
                    }
                });
            }
        }

        private void refactorExtractedMethod() {
            group = new NioEventLoopGroup(new DefaultThreadFactory("arthas-NettyWebsocketTtyBootstrap", true));
        }
}
