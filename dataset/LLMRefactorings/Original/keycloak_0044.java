public class keycloak_0044 {

        private Response makeRequest() throws IOException {
    
            HttpRequestBase httpRequest = createHttpRequest();
    
            if (httpRequest instanceof HttpPost || httpRequest instanceof  HttpPut || httpRequest instanceof HttpPatch) {
                if (params != null) {
                    ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(getFormEntityFromParameter());
                } else if (entity instanceof HttpEntity) {
                    ((HttpEntityEnclosingRequestBase) httpRequest).setEntity((HttpEntity) entity);
                } else if (entity != null) {
                    if (headers == null || !headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
                        header(HttpHeaders.CONTENT_TYPE, "application/json");
                    }
                    ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(getJsonEntity());
                } else {
                    throw new IllegalStateException("No content set");
                }
            }
    
            if (headers != null) {
                for (Map.Entry<String, String> h : headers.entrySet()) {
                    httpRequest.setHeader(h.getKey(), h.getValue());
                }
            }
    
            if (socketTimeOutMillis != UNDEFINED_TIMEOUT) {
                requestConfigBuilder().setSocketTimeout(socketTimeOutMillis);
            }
    
            if (connectTimeoutMillis != UNDEFINED_TIMEOUT) {
                requestConfigBuilder().setConnectTimeout(connectTimeoutMillis);
            }
    
            if (connectionRequestTimeoutMillis != UNDEFINED_TIMEOUT) {
                requestConfigBuilder().setConnectionRequestTimeout(connectionRequestTimeoutMillis);
            }
    
            if (requestConfigBuilder != null) {
                httpRequest.setConfig(requestConfigBuilder.build());
            }
    
            return new Response(client.execute(httpRequest), maxConsumedResponseSize);
        }
}
