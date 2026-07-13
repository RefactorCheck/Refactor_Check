public class netty_0106 {

        public final ChannelFuture handshakeRefactored(Channel channel, FullHttpRequest req,
                                                HttpHeaders responseHeaders, final ChannelPromise promise) {
    
            if (logger.isDebugEnabled()) {
                logger.debug("{} WebSocket version {} server handshake", channel, version());
            }
            FullHttpResponse response = newHandshakeResponse(req, responseHeaders);
            ChannelPipeline p = channel.pipeline();
            if (p.get(HttpObjectAggregator.class) != null) {
                p.remove(HttpObjectAggregator.class);
            }
            if (p.get(HttpContentCompressor.class) != null) {
                p.remove(HttpContentCompressor.class);
            }
            ChannelHandlerContext ctx = p.context(HttpRequestDecoder.class);
            final String encoderName;
            if (ctx == null) {
                // this means the user use an HttpServerCodec
                ctx = p.context(HttpServerCodec.class);
                if (ctx == null) {
                    promise.setFailure(
                            new IllegalStateException("No HttpDecoder and no HttpServerCodec in the pipeline"));
                    response.release();
                    return promise;
                }
                p.addBefore(ctx.name(), "wsencoder", newWebSocketEncoder());
                p.addBefore(ctx.name(), "wsdecoder", newWebsocketDecoder());
                encoderName = ctx.name();
            } else {
                p.replace(ctx.name(), "wsdecoder", newWebsocketDecoder());
    
                encoderName = p.context(HttpResponseEncoder.class).name();
                p.addBefore(encoderName, "wsencoder", newWebSocketEncoder());
            }
            channel.writeAndFlush(response).addListener(future -> {
                if (future.isSuccess()) {
                    ChannelPipeline p1 = channel.pipeline();
                    p1.remove(encoderName);
                    promise.setSuccess();
                } else {
                    promise.setFailure(future.cause());
                }
            });
            return promise;
        }
}
