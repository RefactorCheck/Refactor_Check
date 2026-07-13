public class arthas_0211 {

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof HandshakeComplete) {
                HandshakeComplete handshake = (HandshakeComplete) evt;
                // http request uri
                String uri = handshake.requestUri();
                logger.info("websocket handshake complete, uri: {}", uri);
    
                MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();
                String method = parameters.getFirst(URIConstans.METHOD);
    
                if (MethodConstants.CONNECT_ARTHAS.equals(method)) { // form browser
                    connectArthas(ctx, parameters);
                } else if (MethodConstants.AGENT_REGISTER.equals(method)) { // form arthas agent, register
                    agentRegister(ctx, handshake, uri);
                }
                if (MethodConstants.OPEN_TUNNEL.equals(method)) { // from arthas agent open tunnel
                    String clientConnectionId = parameters.getFirst(URIConstans.CLIENT_CONNECTION_ID);
                    openTunnel(ctx, clientConnectionId);
                }
            } else if (evt instanceof IdleStateEvent) {
                ctx.writeAndFlush(new PingWebSocketFrame());
            } else {
                ctx.fireUserEventTriggered(evt);
            }
        }
}
