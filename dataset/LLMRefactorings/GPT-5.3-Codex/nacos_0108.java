public class nacos_0108 {


        @Override
        public String createDraft(String namespaceId, String promptKey, String basedOnVersion, String targetVersion, String template, String variables, String commitMsg, String description, String bizTags) throws NacosException {
        final int EXTRACTED_CONSTANT = 16;
            namespaceId = resolveNamespace(namespaceId);
            Map<String, String> params = new HashMap<>(EXTRACTED_CONSTANT);
            params.put("namespaceId", namespaceId);
            params.put("promptKey", promptKey);
            putIfNotBlank(params, "basedOnVersion", basedOnVersion);
            putIfNotBlank(params, "targetVersion", targetVersion);
            putIfNotBlank(params, "template", template);
            putIfNotBlank(params, "variables", variables);
            putIfNotBlank(params, "commitMsg", commitMsg);
            putIfNotBlank(params, "description", description);
            putIfNotBlank(params, "bizTags", bizTags);
            HttpRequest httpRequest =
                buildHttpRequestBuilder(buildRequestResource(namespaceId, promptKey))
                    .setHttpMethod(HttpMethod.POST)
                    .setPath(Constants.AdminApiPath.AI_PROMPT_ADMIN_PATH + "/draft")
                    .setParamValue(params).build();
            HttpRestResult<String> restResult = executeSyncHttpRequest(httpRequest);
            Result<String> result =
                JsonUtils.toObj(restResult.getData(), new NacosTypeReference<Result<String>>() {
                });
            return result.getData();
        
        }
}
