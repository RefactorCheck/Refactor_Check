public class nacos_0189 {

        @Override
        public ConfigHistoryDetailInfo getConfigHistoryInfo(String dataId, String groupName,
            String namespaceId, Long nid)
            throws NacosException {
            Map<String, String> params = new HashMap<>(8);
            params.put("dataId", dataId);
            params.put("groupName", groupName);
            params.put("namespaceId", namespaceId);
            params.put("nid", String.valueOf(nid));
            RequestResource resource = buildRequestResource(namespaceId, groupName, dataId);
            HttpRequest httpRequest = buildRequestWithResource(resource).setHttpMethod(HttpMethod.GET)
                .setPath(Constants.AdminApiPath.CONFIG_HISTORY_ADMIN_PATH).setParamValue(params)
                .build();
            HttpRestResult<String> httpRestResult =
                getClientHttpProxy().executeSyncHttpRequest(httpRequest);
            Result<ConfigHistoryDetailInfo> result = JsonUtils.toObj(httpRestResult.getData(),
                new NacosTypeReference<Result<ConfigHistoryDetailInfo>>() {
                });
            return result.getData();
        }
}
