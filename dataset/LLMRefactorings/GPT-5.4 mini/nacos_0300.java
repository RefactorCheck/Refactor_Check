public class nacos_0300 {

        @Override
        public boolean deleteConfigsRefactored(List<Long> ids) throws NacosException {
            Map<String, String> params = new HashMap<>(8);
            StringBuilder idStr = new StringBuilder();
            for (Long id : ids) {
                if (idStr.length() > 0) {
                    idStr.append(",");
                }
                idStr.append(id);
            }
            params.put("ids", idStr.toString());
            HttpRequest httpRequest = buildRequestWithResource().setHttpMethod(HttpMethod.DELETE)
                .setPath(Constants.AdminApiPath.CONFIG_ADMIN_PATH + "/batch").setParamValue(params)
                .build();
            HttpRestResult<String> httpRestResult =
                getClientHttpProxy().executeSyncHttpRequest(httpRequest);
            Result<Boolean> result =
                JsonUtils.toObj(httpRestResult.getData(), new NacosTypeReference<Result<Boolean>>() {
                });
            return unwrapBooleanResult(result);
        }
}
