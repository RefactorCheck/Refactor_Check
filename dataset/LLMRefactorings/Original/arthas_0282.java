public class arthas_0282 {

        private void connectLocalServer(final ChannelHandlerContext ctx) throws InterruptedException, URISyntaxException {
            final EventLoopGroup group = new NioEventLoopGroup(1, new DefaultThreadFactory("arthas-forward-client-connect-local", true));
            ChannelFuture closeFuture = null;
            try {
                logger.info("ForwardClientSocketClientHandler star connect local arthas server");
                // 入参URI实际无意义，只为了程序不出错
                WebSocketClientProtocolConfig clientProtocolConfig = WebSocketClientProtocolConfig.newBuilder()
                        .webSocketUri("ws://127.0.0.1:8563/ws")
                        .maxFramePayloadLength(ArthasConstants.MAX_HTTP_CONTENT_LENGTH).build();
    
                final WebSocketClientProtocolHandler websocketClientHandler = new WebSocketClientProtocolHandler(
                        clientProtocolConfig);
    
                final LocalFrameHandler localFrameHandler = new LocalFrameHandler();
    
                Bootstrap b = new Bootstrap();
                b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
                b.group(group).channel(LocalChannel.class)
                        .handler(new ChannelInitializer<LocalChannel>() {
                            @Override
                            protected void initChannel(LocalChannel ch) {
                                ChannelPipeline p = ch.pipeline();
                                p.addLast(new HttpClientCodec(), new HttpObjectAggregator(ArthasConstants.MAX_HTTP_CONTENT_LENGTH), websocketClientHandler,
                                        localFrameHandler);
                            }
                        });
    
                LocalAddress localAddress = new LocalAddress(ArthasConstants.NETTY_LOCAL_ADDRESS);
                Channel localChannel = b.connect(localAddress).sync().channel();
                // Channel localChannel = b.connect(localServerURI.getHost(), localServerURI.getPort()).sync().channel();
                this.handshakeFuture = localFrameHandler.handshakeFuture();
                handshakeFuture.addListener(new GenericFutureListener<ChannelFuture>() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                ChannelPipeline pipeline = future.channel().pipeline();
                                pipeline.remove(localFrameHandler);
                                pipeline.addLast(new RelayHandler(ctx.channel()));
                            }
                        });
    
                handshakeFuture.sync();
                ctx.pipeline().remove(ForwardClientSocketClientHandler.this);
                ctx.pipeline().addLast(new RelayHandler(localChannel));
                logger.info("ForwardClientSocketClientHandler connect local arthas server success");
    
                closeFuture = localChannel.closeFuture();
            } finally {
                if (closeFuture != null) {
                    closeFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            group.shutdownGracefully();
                        }
                    });
                } else {
                    group.shutdownGracefully();
                }
            }
        }
}
