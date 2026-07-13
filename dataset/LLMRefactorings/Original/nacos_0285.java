public class nacos_0285 {

        private HttpRestResult<String> callServerStringWithHeader(String api,
            Map<String, String> params, String server, RequestResource resource)
            throws NacosException {
            Map<String, String> securityHeaders = securityProxy.getIdentityContext(resource);
            Header header = Header.newInstance();
            header.addAll(securityHeaders);
            
            String url = buildUrl(server, api);
            
            try {
                HttpRestResult<String> restResult = nacosRestTemplate.get(url, header,
                    Query.newInstance().initParams(params), String.class);
                
                if (restResult.ok()) {
                    return restResult;
                }
                if (HttpURLConnection.HTTP_NOT_MODIFIED == restResult.getCode()) {
                    throw new NacosException(NacosException.NOT_MODIFIED, "not modified");
                }
                if (HttpURLConnection.HTTP_FORBIDDEN == restResult.getCode()) {
                    securityProxy.reLogin();
                }
                throw new NacosException(restResult.getCode(), restResult.getMessage());
            } catch (NacosException e) {
                throw e;
            } catch (Exception e) {
                LOGGER.error("[AI-HTTP] Failed to request {}", url, e);
                throw new NacosException(NacosException.SERVER_ERROR, e);
            }
        }
}
