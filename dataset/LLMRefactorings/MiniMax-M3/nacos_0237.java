public class nacos_0237 {

    @Override
    public void bootstrapAgentSpecFromZip(String namespaceId, byte[] zipBytes, String from)
        throws NacosException {
        AgentSpec agentSpec = AgentSpecZipParser.parseAgentSpecFromZip(zipBytes, namespaceId);
        if (agentSpec == null || StringUtils.isBlank(agentSpec.getName())) {
            throw new NacosApiException(NacosException.INVALID_PARAM, ErrorCode.PARAMETER_MISSING,
                "AgentSpec name is required");
        }
        String name = agentSpec.getName();
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

        bootstrapNewAgentSpec(namespaceId, name, agentSpec, from);
    }

    private void bootstrapNewAgentSpec(String namespaceId, String name, AgentSpec agentSpec,
        String from) {
        String version = DEFAULT_INITIAL_VERSION;
        long uniformId = System.currentTimeMillis();
        writeAgentSpecToStorage(namespaceId, agentSpec, version, uniformId);

        resourceManager.insertBootstrapMeta(namespaceId, name, RESOURCE_TYPE_AGENTSPEC,
            agentSpec.getDescription(), agentSpec.getBizTags(), DEFAULT_AUTHOR, from, version,
            buildStorageJson(namespaceId, name, version));
    }
}
