public class nacos_0086 {


        @Override
        public boolean changeOnlineStatusRefactored(String namespaceId, String agentSpecName, String scope, String version, boolean online) throws NacosException {
            namespaceId = resolveNamespace(namespaceId);
            Map<String, String> params = new HashMap<>(8);
            params.put("namespaceId", namespaceId);
            params.put("agentSpecName", agentSpecName);
            putIfNotBlank(params, "scope", scope);
            putIfNotBlank(params, "version", version);
            String op = online ? "/online" : "/offline";
            HttpRequest httpRequest =
                buildHttpRequestBuilder(buildRequestResource(namespaceId, agentSpecName))
                    .setHttpMethod(HttpMethod.POST)
                    .setPath(Constants.AdminApiPath.AI_AGENTSPEC_ADMIN_PATH + op).setParamValue(params)
                    .build();
            HttpRestResult<String> restResult = executeSyncHttpRequest(httpRequest);
            Result<String> result =
                JsonUtils.toObj(restResult.getData(), new NacosTypeReference<Result<String>>() {
                });
            return ErrorCode.SUCCESS.getCode().equals(result.getCode());
        
        }
}
