public class nacos_0252 {

        @Override
        public String createDraft(String namespaceId, String agentSpecName, String basedOnVersion,
            String targetVersion)
            throws NacosException {
            namespaceId = resolveNamespace(namespaceId);
            Map<String, String> params = buildDraftParams(namespaceId, agentSpecName, basedOnVersion, targetVersion);
            HttpRequest httpRequest =
                buildHttpRequestBuilder(buildRequestResource(namespaceId, agentSpecName))
                    .setHttpMethod(HttpMethod.POST)
                    .setPath(Constants.AdminApiPath.AI_AGENTSPEC_ADMIN_PATH + "/draft")
                    .setParamValue(params).build();
            HttpRestResult<String> restResult = executeSyncHttpRequest(httpRequest);
            Result<String> result =
                JsonUtils.toObj(restResult.getData(), new NacosTypeReference<Result<String>>() {
                });
            return result.getData();
        }

        private Map<String, String> buildDraftParams(String namespaceId, String agentSpecName,
                String basedOnVersion, String targetVersion) {
            Map<String, String> params = new HashMap<>(8);
            params.put("namespaceId", namespaceId);
            params.put("agentSpecName", agentSpecName);
            putIfNotBlank(params, "basedOnVersion", basedOnVersion);
            putIfNotBlank(params, "targetVersion", targetVersion);
            return params;
        }
}
