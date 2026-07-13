public class dubbo_0122 {

        @Override
        public void doOpen0() {
            bootstrap = new ServerBootstrap();
    
            // initialize dubboChannels and serverShutdownTimeoutMills before potential usage to avoid NPE.
            dubboChannels = new ConcurrentHashMap<>();
    
            // you can customize name and type of client thread pool by THREAD_NAME_KEY and THREADPOOL_KEY in
            // CommonConstants.
            // the handler will be wrapped: MultiMessageHandler->HeartbeatHandler->handler
            // read config before destroy
            serverShutdownTimeoutMills = ConfigurationUtils.getServerShutdownTimeout(getUrl().getOrDefaultModuleModel());
    
            bossGroup = NettyEventLoopFactory.eventLoopGroup(1, EVENT_LOOP_BOSS_POOL_NAME);
            workerGroup = NettyEventLoopFactory.eventLoopGroup(
                    getUrl().getPositiveParameter(IO_THREADS_KEY, Constants.DEFAULT_IO_THREADS),
                    EVENT_LOOP_WORKER_POOL_NAME);
    
            bootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NettyEventLoopFactory.serverSocketChannelClass())
                    .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                    .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            initChannelPipeline(ch);
                        }
                    });
            // bind
    
            String bindIp = getUrl().getParameter(Constants.BIND_IP_KEY, getUrl().getHost());
            int bindPort = getUrl().getParameter(Constants.BIND_PORT_KEY, getUrl().getPort());
            if (getUrl().getParameter(ANYHOST_KEY, false) || NetUtils.isInvalidLocalHost(bindIp)) {
                bindIp = ANYHOST_VALUE;
            }
            InetSocketAddress bindAddress = new InetSocketAddress(bindIp, bindPort);
            try {
                ChannelFuture channelFuture = bootstrap.bind(bindAddress);
                channelFuture.syncUninterruptibly();
                channel = channelFuture.channel();
            } catch (Throwable t) {
                closeBootstrap();
                throw t;
            }
        }

        private void initChannelPipeline(SocketChannel ch) throws Exception {
            // Do not add idle state handler here, because it should be added in the protocol handler.
            final ChannelPipeline p = ch.pipeline();
            NettyChannelHandler nettyChannelHandler =
                    new NettyChannelHandler(dubboChannels, getUrl(), NettyPortUnificationServer.this);
            NettyPortUnificationServerHandler puHandler = new NettyPortUnificationServerHandler(
                    getUrl(),
                    true,
                    getProtocols(),
                    NettyPortUnificationServer.this,
                    getSupportedUrls(),
                    getSupportedHandlers());
            p.addLast("channel-handler", nettyChannelHandler);
            p.addLast("negotiation-protocol", puHandler);
        }
}
