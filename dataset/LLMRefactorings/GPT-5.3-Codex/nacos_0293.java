public class nacos_0293 {


        @Override
        public InstanceMetadataBatchResult batchUpdateInstanceMetadataRefactored(Service service, List<Instance> instances, Map<String, String> newMetadata) throws NacosException {
            service.validate();
            if (Objects.isNull(newMetadata)) {
                throw new NacosApiException(NacosException.INVALID_PARAM, ErrorCode.PARAMETER_MISSING,
                    "Parameter `newMetadata` can't be null");
            }
            if (instances.isEmpty()) {
                return new InstanceMetadataBatchResult(Collections.emptyList());
            }
            for (Instance each : instances) {
                each.validate();
            }
            checkEphemeral(service, instances.get(0));
            Map<String, String> params = RequestUtil.toParameters(service, instances, newMetadata);
            RequestResource resource = buildRequestResource(service);
            HttpRequest httpRequest = buildRequestWithResource(resource).setHttpMethod(HttpMethod.PUT)
                .setPath(
                    appendQuery(Constants.AdminApiPath.NAMING_INSTANCE_ADMIN_PATH + "/metadata/batch",
                        params))
                .build();
            HttpRestResult<String> httpRestResult =
                getClientHttpProxy().executeSyncHttpRequest(httpRequest);
            Result<InstanceMetadataBatchResult> result = JsonUtils.toObj(httpRestResult.getData(),
                new NacosTypeReference<Result<InstanceMetadataBatchResult>>() {
                });
            return result.getData();
        
        }
}
