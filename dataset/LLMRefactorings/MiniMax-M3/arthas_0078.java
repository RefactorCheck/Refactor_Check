public class arthas_0078 {

        private FullHttpResponse forwardRequest(FullHttpRequest request, String address) {
            OkHttpClient client = buildOkHttpClient();
            Request okRequest = buildOkRequest(address, request);

            try {
                Response response = client.newCall(okRequest).execute();

                if (response.isSuccessful()) {
                    return buildSuccessResponse(request.getProtocolVersion(), response);
                } else {
                    return buildErrorResponse(HttpResponseStatus.valueOf(response.code()), "Error: " + response.message());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return buildErrorResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Error forwarding request: " + e.getMessage());
            }
        }

        private OkHttpClient buildOkHttpClient() {
            return new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
        }

        private Request buildOkRequest(String address, FullHttpRequest request) {
            String url = "http://" + address + "/api/native-agent";
            RequestBody requestBody = RequestBody.create(
                    request.content().toString(CharsetUtil.UTF_8),
                    MediaType.parse("application/json; charset=utf-8")
            );
            return new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        }

        private FullHttpResponse buildSuccessResponse(HttpVersion protocolVersion, Response response) {
            String responseBody = response.body().string();
            DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
                    protocolVersion,
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer(responseBody, StandardCharsets.UTF_8)
            );
            setCorsHeaders(fullHttpResponse);
            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
            return fullHttpResponse;
        }

        private void setCorsHeaders(FullHttpResponse response) {
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "X-Requested-With, Content-Type, Authorization");
        }

        private FullHttpResponse buildErrorResponse(HttpResponseStatus status, String message) {
            return new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    status,
                    Unpooled.copiedBuffer(message, CharsetUtil.UTF_8)
            );
        }
}
