public class nacos_0237 {

        @Override
        public void bootstrapAgentSpecFromZipRefactored(String namespaceId, byte[] zipBytes, String from)
            throws NacosException {
            // Step 1: Parse ZIP and validate
            AgentSpec agentSpec = AgentSpecZipParser.parseAgentSpecFromZip(zipBytes, namespaceId);
            if (agentSpec == null || StringUtils.isBlank(agentSpec.getName())) {
                throw new NacosApiException(NacosException.INVALID_PARAM, ErrorCode.PARAMETER_MISSING,
                    "AgentSpec name is required");
            }
            String name = agentSpec.getName();
            // Step 2: If already exists, try to repair broken built-in data; otherwise skip
            AiResource existingMeta =
                resourceManager.findMeta(namespaceId, name, RESOURCE_TYPE_AGENTSPEC);
            if (existingMeta != null) {
                if (repairBuiltInAgentSpecIfBroken(namespaceId, existingMeta, agentSpec)) {
                    LOGGER.info("Repaired built-in agentspec bootstrap content for {}", name);
                    return;
                }
                LOGGER.info("Skip built-in agentspec bootstrap because agentspec already exists: {}",
                    name);
                return;
            }
            
            // Step 3: Brand-new bootstrap: write to storage and directly create published meta + version (skip draft workflow)
            String version = DEFAULT_INITIAL_VERSION;
            long uniformId = System.currentTimeMillis();
            writeAgentSpecToStorage(namespaceId, agentSpec, version, uniformId);
            
            resourceManager.insertBootstrapMeta(namespaceId, name, RESOURCE_TYPE_AGENTSPEC,
                agentSpec.getDescription(), agentSpec.getBizTags(), DEFAULT_AUTHOR, from, version,
                buildStorageJson(namespaceId, name, version));
        }
}
