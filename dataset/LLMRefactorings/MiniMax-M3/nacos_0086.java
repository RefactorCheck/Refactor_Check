public class nacos_0086 {

        @Override
        public boolean changeOnlineStatus(String namespaceId, String agentSpecName, String scope,
            String version,
            boolean online) throws NacosException {
            namespaceId = resolveNamespace(namespaceId);
            Map<String, String> params = buildParams(namespaceId, agentSpecName, scope, version);
            String op = online ? "/online" : "/offline";
            HttpRequest httpRequest = buildHttpRequest(namespaceId, agentSpecName, op, params);
            return executeAndCheckSuccess(httpRequest);
        }

        private Map<String, String> buildParams(String namespaceId, String agentSpecName, String scope,
            String version) {
            Map<String, String> params = new HashMap<>(8);
            params.put("namespaceId", namespaceId);
            params.put("agentSpecName", agentSpecName);
            putIfNotBlank(params, "scope", scope);
            putIfNotBlank(params, "version", version);
            return params;
        }

        private HttpRequest buildHttpRequest(String namespaceId, String agentSpecName, String op,
            Map<String, String> params) {
            return buildHttpRequestBuilder(buildRequestResource(namespaceId, agentSpecName))
                .setHttpMethod(HttpMethod.POST)
                .setPath(Constants.AdminApiPath.AI_AGENTSPEC_ADMIN_PATH + op).setParamValue(params)
                .build();
        }

        private boolean executeAndCheckSuccess(HttpRequest httpRequest) throws NacosException {
            HttpRestResult<String> restResult = executeSyncHttpRequest(httpRequest);
            Result<String> result =
                JsonUtils.toObj(restResult.getData(), new NacosTypeReference<Result<String>>() {
                });
            return ErrorCode.SUCCESS.getCode().equals(result.getCode());
        }
}
