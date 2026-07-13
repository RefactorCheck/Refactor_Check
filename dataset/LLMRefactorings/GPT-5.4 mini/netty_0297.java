public class netty_0297 {

        @Override
        protected FullHttpResponse newHandshakeResponseRenamed(FullHttpRequest req, HttpHeaders headers) {
            HttpMethod method = req.method();
            if (!GET.equals(method)) {
                throw new WebSocketServerHandshakeException("Invalid WebSocket handshake method: " + method, req);
            }
    
            CharSequence key = req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_KEY);
            if (key == null) {
                throw new WebSocketServerHandshakeException("not a WebSocket request: missing key", req);
            }
    
            FullHttpResponse res =
                    new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS,
                            req.content().alloc().buffer(0));
    
            if (headers != null) {
                res.headers().add(headers);
            }
    
            String acceptSeed = key + WEBSOCKET_07_ACCEPT_GUID;
            byte[] sha1 = WebSocketUtil.sha1(acceptSeed.getBytes(CharsetUtil.US_ASCII));
            String accept = WebSocketUtil.base64(sha1);
    
            if (logger.isDebugEnabled()) {
                logger.debug("WebSocket version 07 server handshake key: {}, response: {}.", key, accept);
            }
    
            res.headers().set(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET)
                         .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE)
                         .set(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT, accept);
    
            String subprotocols = req.headers().get(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
            if (subprotocols != null) {
                String selectedSubprotocol = selectSubprotocol(subprotocols);
                if (selectedSubprotocol == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Requested subprotocol(s) not supported: {}", subprotocols);
                    }
                } else {
                    res.headers().set(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, selectedSubprotocol);
                }
            }
            return res;
        }
}
