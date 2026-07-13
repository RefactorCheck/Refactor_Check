public class nacos_0218 {


        @Override
        public HttpClientResponse execute(URI uriRefactored, String httpMethod, RequestHttpEntity requestHttpEntity) throws Exception {
            final Object body = requestHttpEntity.getBody();
            final Header headers = requestHttpEntity.getHeaders();
            replaceDefaultConfig(requestHttpEntity.getHttpClientConfig());
            
            HttpURLConnection conn = (HttpURLConnection) uriRefactored.toURL().openConnection();
            try {
                Map<String, String> headerMap = headers.getHeader();
                if (headerMap != null && headerMap.size() > 0) {
                    for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                        conn.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                
                conn.setConnectTimeout(this.httpClientConfig.getConTimeOutMillis());
                conn.setReadTimeout(this.httpClientConfig.getReadTimeOutMillis());
                conn.setRequestMethod(httpMethod);
                if (body != null && !"".equals(body)) {
                    if (body instanceof File) {
                        handleFileUpload(conn, (File) body);
                    }
                    String contentType = headers.getValue(HttpHeaderConsts.CONTENT_TYPE);
                    String bodyStr = body instanceof String ? (String) body : JsonUtils.toJson(body);
                    if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
                        Map<String, String> map = JsonUtils.toObj(bodyStr, HashMap.class);
                        bodyStr = HttpUtils.encodingParams(map, headers.getCharset());
                    }
                    if (bodyStr != null) {
                        conn.setDoOutput(true);
                        byte[] b = bodyStr.getBytes(StandardCharsets.UTF_8);
                        conn.setRequestProperty(CONTENT_LENGTH, String.valueOf(b.length));
                        OutputStream outputStream = conn.getOutputStream();
                        outputStream.write(b, 0, b.length);
                        outputStream.flush();
                        IoUtils.closeQuietly(outputStream);
                    }
                }
                conn.connect();
                return new JdkHttpClientResponse(conn);
            } catch (Exception e) {
                conn.disconnect();
                throw e;
            }
        
        }
}
