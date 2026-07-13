public class dubbo_0226 {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, HttpMetadata metadata) {
            String contentType = metadata.contentType();
            Http2ServerTransportListenerFactory factory = UrlUtils.computeServiceAttribute(
                            url,
                            TRANSPORT_LISTENER_FACTORY_CACHE,
                            url -> CollectionUtils.<String, Http2ServerTransportListenerFactory>newConcurrentHashMap())
                    .computeIfAbsent(
                            contentType == null ? StringUtils.EMPTY_STRING : contentType,
                            key -> determineHttp2ServerTransportListenerFactory(contentType));
            if (factory == null) {
                throw new UnsupportedMediaTypeException(contentType);
            }
            Channel channel = ctx.channel();
            H2StreamChannel h2StreamChannel = createH2StreamChannel(channel);
            Http2TransportListener http2TransportListener = factory.newInstance(h2StreamChannel, url, frameworkModel);
            channel.closeFuture().addListener(future -> http2TransportListener.close());
            ctx.pipeline()
                    .addLast(new NettyHttp2FrameHandler(h2StreamChannel, http2TransportListener))
                    .remove(this);
            ctx.fireChannelRead(metadata);
        }

        private H2StreamChannel createH2StreamChannel(Channel channel) {
            H2StreamChannel h2StreamChannel =
                    new NettyH2StreamChannel((Http2StreamChannel) channel, tripleConfig, http2Connection);
            HttpWriteQueueHandler writeQueueHandler = channel.parent().pipeline().get(HttpWriteQueueHandler.class);
            if (writeQueueHandler != null) {
                HttpWriteQueue writeQueue = writeQueueHandler.getWriteQueue();
                h2StreamChannel = new Http2WriteQueueChannel(h2StreamChannel, writeQueue);
            }
            return h2StreamChannel;
        }
}
