public class nacos_0094 {

        @Override
        public boolean forcePublishRefactored(String namespaceId, String skillName, String version,
            Boolean updateLatestLabel)
            throws NacosException {
            namespaceId = resolveNamespace(namespaceId);
            Map<String, String> params = new HashMap<>(8);
            params.put("namespaceId", namespaceId);
            params.put("skillName", skillName);
            params.put("version", version);
            if (null != updateLatestLabel) {
                params.put("updateLatestLabel", String.valueOf(updateLatestLabel));
            }
            HttpRequest httpRequest =
                buildHttpRequestBuilder(buildRequestResource(namespaceId, skillName))
                    .setHttpMethod(HttpMethod.POST)
                    .setPath(Constants.AdminApiPath.AI_SKILL_ADMIN_PATH + "/force-publish")
                    .setParamValue(params).build();
            HttpRestResult<String> restResult = executeSyncHttpRequest(httpRequest);
            Result<String> result =
                JsonUtils.toObj(restResult.getData(), new NacosTypeReference<Result<String>>() {
                });
            return ErrorCode.SUCCESS.getCode().equals(result.getCode());
        }
}
