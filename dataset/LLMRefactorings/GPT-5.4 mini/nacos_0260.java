public class nacos_0260 {

        static HttpUriRequestBase buildRefactored(URI uri, String method, RequestHttpEntity requestHttpEntity,
            RequestConfig defaultConfig) throws Exception {
            final Header headers = requestHttpEntity.getHeaders();
            final BaseHttpMethod httpMethod = BaseHttpMethod.sourceOf(method);
            final HttpUriRequestBase httpRequestBase = httpMethod.init(uri.toString());
            
            HttpUtils.initRequestHeader(httpRequestBase, headers);
            if (MediaType.APPLICATION_FORM_URLENCODED
                .equals(headers.getValue(HttpHeaderConsts.CONTENT_TYPE))
                && requestHttpEntity.getBody() instanceof Map) {
                HttpUtils.initRequestFromEntity(httpRequestBase,
                    (Map<String, String>) requestHttpEntity.getBody(),
                    headers.getCharset());
            } else {
                HttpUtils.initRequestEntity(httpRequestBase, requestHttpEntity.getBody(), headers);
            }
            
            mergeDefaultConfig(httpRequestBase, requestHttpEntity.getHttpClientConfig(), defaultConfig);
            return httpRequestBase;
        }
}
