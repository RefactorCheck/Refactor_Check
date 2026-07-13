public class nacos_0273 {

        @Override
        public void startServer() throws Exception {
            final MutableHandlerRegistry handlerRegistry = new MutableHandlerRegistry();
            addServices(handlerRegistry, getSeverInterceptors().toArray(new ServerInterceptor[0]));
            String grpcListenIp = InetUtils.getGrpcListenIp();
            InetSocketAddress inetSocketAddress = StringUtils.isNotBlank(grpcListenIp)
                ? new InetSocketAddress(grpcListenIp, getServicePort())
                : new InetSocketAddress(getServicePort());
            NettyServerBuilder builder =
                NettyServerBuilder.forAddress(inetSocketAddress).executor(getRpcExecutor());
            Optional<InternalProtocolNegotiator.ProtocolNegotiator> negotiator =
                newProtocolNegotiator();
            if (negotiator.isPresent()) {
                InternalProtocolNegotiator.ProtocolNegotiator actual = negotiator.get();
                Loggers.REMOTE.info("Add protocol negotiator {}", actual.getClass().getCanonicalName());
                builder.protocolNegotiator(actual);
            }
            
            for (ServerTransportFilter each : getServerTransportFilters()) {
                builder.addTransportFilter(each);
            }
            server = builder.maxInboundMessageSize(getMaxInboundMessageSize())
                .fallbackHandlerRegistry(handlerRegistry)
                .compressorRegistry(CompressorRegistry.getDefaultInstance())
                .decompressorRegistry(DecompressorRegistry.getDefaultInstance())
                .keepAliveTime(getKeepAliveTime(), TimeUnit.MILLISECONDS)
                .keepAliveTimeout(getKeepAliveTimeout(), TimeUnit.MILLISECONDS)
                .permitKeepAliveTime(getPermitKeepAliveTime(), TimeUnit.MILLISECONDS).build();
            
            server.start();
        }
}
