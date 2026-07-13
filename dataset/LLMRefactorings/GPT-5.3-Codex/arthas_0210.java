public class arthas_0210 {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
            // 只有 arthas agent register建立的 channel 才可能有数据到这里
            if (frame instanceof TextWebSocketFrame) {
                String text = ((TextWebSocketFrame) frame).text();
    
                MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(text).build()
                        .getQueryParams();
    
                String method = parameters.getFirst(URIConstans.METHOD);
    
                /**
                 * <pre>
                 * 1. 之前http proxy请求已发送到 tunnel cleint，这里接收到 tunnel client的结果，并解析出SimpleHttpResponse
                 * 2. 需要据 URIConstans.PROXY_REQUEST_ID 取出当时的 Promise，再设置SimpleHttpResponse进去
                 * </pre>
                 */
                if (MethodConstants.HTTP_PROXY.equals(method)) {
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
        }
}
