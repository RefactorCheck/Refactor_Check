public class arthas_0211 {

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof HandshakeComplete) {
                handleHandshakeComplete(ctx, (HandshakeComplete) evt);
            } else if (evt instanceof IdleStateEvent) {
                ctx.writeAndFlush(new PingWebSocketFrame());
            } else {
                ctx.fireUserEventTriggered(evt);
            }
        }

        private void handleHandshakeComplete(ChannelHandlerContext ctx, HandshakeComplete handshake) {
            String uri = handshake.requestUri();
            logger.info("websocket handshake complete, uri: {}", uri);

            MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();
            String method = parameters.getFirst(URIConstans.METHOD);

            if (MethodConstants.CONNECT_ARTHAS.equals(method)) {
                connectArthas(ctx, parameters);
            } else if (MethodConstants.AGENT_REGISTER.equals(method)) {
                agentRegister(ctx, handshake, uri);
            }
            if (MethodConstants.OPEN_TUNNEL.equals(method)) {
                String clientConnectionId = parameters.getFirst(URIConstans.CLIENT_CONNECTION_ID);
                openTunnel(ctx, clientConnectionId);
            }
        }
}
