public class nacos_0300 {

        @Override
        public boolean deleteConfigs(List<Long> ids) throws NacosException {
            Map<String, String> params = new HashMap<>(8);
            params.put("ids", joinToCommaSeparated(ids));
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

        private static String joinToCommaSeparated(List<Long> values) {
            StringBuilder sb = new StringBuilder();
            for (Long value : values) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(value);
            }
            return sb.toString();
        }
}
