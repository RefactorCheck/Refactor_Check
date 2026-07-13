public class arthas_0210 {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
                String text = textFrame.text();
    
                MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(text).build()
                        .getQueryParams();
    
                String method = parameters.getFirst(URIConstans.METHOD);
    
                if (MethodConstants.HTTP_PROXY.equals(method)) {
                    handleHttpProxy(parameters, text);
                }
            }
        }

        private void handleHttpProxy(MultiValueMap<String, String> parameters, String text) throws Exception {
            final String requestIdRaw = parameters.getFirst(URIConstans.PROXY_REQUEST_ID);
            final String requestId;
            if (requestIdRaw != null) {
                requestId = URLDecoder.decode(requestIdRaw, "utf-8");
            } else {
                requestId = null;
            }
            if (requestId == null) {
                logger.error("error, need {}, text: {}", URIConstans.PROXY_REQUEST_ID, text);
                return;
            }
            logger.info("received http proxy response, requestId: {}", requestId);

            Promise<SimpleHttpResponse> promise = tunnelServer.findProxyRequestPromise(requestId);

            final String dataRaw = parameters.getFirst(URIConstans.PROXY_RESPONSE_DATA);
            final String data;
            if (dataRaw != null) {
                data = URLDecoder.decode(dataRaw, "utf-8");
                byte[] bytes = Base64.decodeBase64(data);

                SimpleHttpResponse simpleHttpResponse = SimpleHttpResponse.fromBytes(bytes);
                promise.setSuccess(simpleHttpResponse);
            } else {
                data = null;
                promise.setFailure(new Exception(URIConstans.PROXY_RESPONSE_DATA + " is null! reuqestId: " + requestId));
            }
        }
}
