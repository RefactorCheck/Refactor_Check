public class nacos_0281 {

        @Override
        public Page<PromptMetaSummary> listPrompts(String namespaceId, String promptKey, String search,
            String bizTags,
            int pageNo, int pageSize) throws NacosException {
            namespaceId = resolveNamespace(namespaceId);
            Map<String, String> params = new HashMap<>(8);
            params.put("namespaceId", namespaceId);
            params.put("promptKey", promptKey);
            params.put("search", search);
            putIfNotBlank(params, "bizTags", bizTags);
            params.put("pageNo", String.valueOf(pageNo));
            params.put("pageSize", String.valueOf(pageSize));
            HttpRequest httpRequest =
                buildHttpRequestBuilder(buildRequestResource(namespaceId, promptKey))
                    .setHttpMethod(HttpMethod.GET)
                    .setPath(Constants.AdminApiPath.AI_PROMPT_LIST_ADMIN_PATH)
                    .setParamValue(params).build();
            HttpRestResult<String> restResult = executeSyncHttpRequest(httpRequest);
            Result<Page<PromptMetaSummary>> result = JsonUtils.toObj(restResult.getData(),
                new NacosTypeReference<Result<Page<PromptMetaSummary>>>() {
                });
            return result.getData();
        }
}
