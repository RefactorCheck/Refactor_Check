public class arthas_0078 {

        private FullHttpResponse forwardRequest(FullHttpRequest request, String address) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
    
            String url = "http://" + address + "/api/native-agent";
    
            RequestBody requestBody = RequestBody.create(
                    request.content().toString(CharsetUtil.UTF_8),
                    MediaType.parse("application/json; charset=utf-8")
            );
    
            Request okRequest = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
    
            try {
                Response response = client.newCall(okRequest).execute();
    
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
                            request.getProtocolVersion(),
                            HttpResponseStatus.OK,
                            Unpooled.copiedBuffer(responseBody, StandardCharsets.UTF_8)
                    );
                    // 设置跨域响应头
                    fullHttpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                    fullHttpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
                    fullHttpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "X-Requested-With, Content-Type, Authorization");
    
                    // 设置其他必要的头部
                    fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
                    fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
                    return fullHttpResponse;
                } else {
                    return new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1,
                            HttpResponseStatus.valueOf(response.code()),
                            Unpooled.copiedBuffer("Error: " + response.message(), CharsetUtil.UTF_8)
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.INTERNAL_SERVER_ERROR,
                        Unpooled.copiedBuffer("Error forwarding request: " + e.getMessage(), CharsetUtil.UTF_8)
                );
            }
        }
}
