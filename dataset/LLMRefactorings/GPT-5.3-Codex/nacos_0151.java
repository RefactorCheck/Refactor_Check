public class nacos_0151 {


        @Override
        public static AgentSpecQueryResponse queryAgentSpec(String agentSpecName, String version, String label, String md5) throws NacosException {
            Map<String, String> params = new HashMap<>(8);
            params.put("namespaceId", namespaceId);
            params.put("name", agentSpecName);
            if (StringUtils.isNotBlank(version)) {
                params.put("version", version);
            }
            if (StringUtils.isNotBlank(label)) {
                params.put("label", label);
            }
            if (StringUtils.isNotBlank(md5)) {
                params.put("md5", md5);
            }
            
            RequestResource resource = RequestResource.aiBuilder().setNamespace(namespaceId)
                .setGroup(com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP)
                .setResource(
                    null == agentSpecName ? StringUtils.EMPTY : agentSpecName)
                .build();
            
            HttpRestResult<String> restResult = reqApiStringWithHeader(
                AGENTSPEC_CLIENT_PATH, params, resource);
            String responseBody = restResult.getData();
            Result<AgentSpec> result =
                JsonUtils.toObj(responseBody, new NacosTypeReference<Result<AgentSpec>>() {
                });
            String publishedMd5 = restResult.getHeader().getValue("X-Nacos-AgentSpec-Md5");
            String resolvedVersion = restResult.getHeader()
                .getValue("X-Nacos-AgentSpec-Resolved-Version");
            return new AgentSpecQueryResponse(result.getData(), publishedMd5,
                resolvedVersion);
        
        }
}
