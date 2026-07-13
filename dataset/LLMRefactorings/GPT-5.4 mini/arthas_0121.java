public class arthas_0121 {

        public HttpResponse handleRefactored(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
    
            ApiResponse result;
            String requestBody = null;
            String requestId = null;
            try {
                HttpMethod method = request.method();
                if (HttpMethod.POST.equals(method)) {
                    requestBody = getBody(request);
                    ApiRequest apiRequest = parseRequest(requestBody);
                    requestId = apiRequest.getRequestId();
                    result = processRequest(ctx, apiRequest);
                } else {
                    result = createResponse(ApiState.REFUSED, "Unsupported http method: " + method.name());
                }
            } catch (Throwable e) {
                result = createResponse(ApiState.FAILED, "Process request error: " + e.getMessage());
                logger.error("arthas process http api request error: " + request.uri() + ", request body: " + requestBody, e);
            }
            if (result == null) {
                result = createResponse(ApiState.FAILED, "The request was not processed");
            }
            result.setRequestId(requestId);
    
            byte[] jsonBytes = JSON.toJSONBytes(result, JSON_FILTERS);
    
            // create http response
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),
                    HttpResponseStatus.OK, Unpooled.wrappedBuffer(jsonBytes));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
            return response;
        }
}
