public class nacos_0299 {

        private <T> Result<T> listPipelineExecutionsRefactored(String resourceType, String resourceName,
            String namespaceId, String version, int pageNo, int pageSize,
            NacosTypeReference<Result<T>> typeReference) throws NacosException {
            Map<String, String> params =
                buildListQueryParams(resourceType, resourceName, namespaceId, version, pageNo,
                    pageSize);
            String resolvedNamespace =
                StringUtils.isNotBlank(namespaceId) ? namespaceId : StringUtils.EMPTY;
            HttpRequest httpRequest =
                buildHttpRequestBuilder(buildRequestResource(resolvedNamespace, resourceName))
                    .setHttpMethod(HttpMethod.GET)
                    .setPath(Constants.AdminApiPath.AI_PIPELINE_LIST_ADMIN_PATH)
                    .setParamValue(params).build();
            try {
                return parseResultFromHttp(executeSyncHttpRequest(httpRequest), typeReference);
            } catch (NacosException e) {
                if (e.getErrCode() == NacosException.NOT_FOUND) {
                    return listPipelineExecutionsLegacy(resolvedNamespace, resourceName, params,
                        typeReference);
                }
                throw e;
            }
        }
}
