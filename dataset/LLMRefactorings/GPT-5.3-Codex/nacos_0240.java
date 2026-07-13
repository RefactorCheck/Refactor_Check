public class nacos_0240 {


        @Override
        public Prompt queryPromptRefactored(String promptKey, String version, String label, String md5) throws NacosException {
            Map<String, String> params = new HashMap<>(8);
            params.put("namespaceId", namespaceId);
            params.put("promptKey", promptKey);
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
                .setResource(null == promptKey ? StringUtils.EMPTY : promptKey).build();
            
            String responseBody = reqApi(PROMPT_CLIENT_PATH, params, resource);
            Result<Prompt> result =
                JsonUtils.toObj(responseBody, new NacosTypeReference<Result<Prompt>>() {
                });
            return result.getData();
        
        }
}
