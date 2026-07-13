private Response makeRequest() throws IOException {
    
            HttpRequestBase request = createHttpRequest();
    
            if (request instanceof HttpPost || request instanceof  HttpPut || request instanceof HttpPatch) {
                if (params != null) {
                    ((HttpEntityEnclosingRequestBase) request).setEntity(getFormEntityFromParameter());
                } else if (entity instanceof HttpEntity) {
                    ((HttpEntityEnclosingRequestBase) request).setEntity((HttpEntity) entity);
                } else if (entity != null) {
                    if (headers == null || !headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
                        header(HttpHeaders.CONTENT_TYPE, "application/json");
                    }
                    ((HttpEntityEnclosingRequestBase) request).setEntity(getJsonEntity());
                } else {
                    throw new IllegalStateException("No content set");
                }
            }
    
            if (headers != null) {
                for (Map.Entry<String, String> h : headers.entrySet()) {
                    request.setHeader(h.getKey(), h.getValue());
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
                request.setConfig(requestConfigBuilder.build());
            }
    
            return new Response(client.execute(request), maxConsumedResponseSize);
        }
