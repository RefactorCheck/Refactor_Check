public class nacos_0216 {

    private static byte[] buildMainContent(AgentSpec agentSpec, long uniformId) {
        AgentSpecMainConfig mainConfig = new AgentSpecMainConfig();
        mainConfig.setName(agentSpec.getName());
        mainConfig.setDescription(agentSpec.getDescription());
        mainConfig.setContent(agentSpec.getContent());
        mainConfig.setUniformId(uniformId);
        mainConfig.setResources(buildResourceRefs(agentSpec));
        return JacksonUtils.toJson(mainConfig).getBytes(StandardCharsets.UTF_8);
    }

    private static List<AgentSpecResourceRef> buildResourceRefs(AgentSpec agentSpec) {
        List<AgentSpecResourceRef> resourceRefs = new ArrayList<>(
                agentSpec.getResource() != null ? agentSpec.getResource().size() : 16);
        if (agentSpec.getResource() != null) {
            for (Map.Entry<String, AgentSpecResource> entry : agentSpec.getResource().entrySet()) {
                AgentSpecResource resource = entry.getValue();
                AgentSpecResourceRef ref = new AgentSpecResourceRef();
                ref.setName(resource.getName());
                ref.setType(resource.getType());
                resourceRefs.add(ref);
            }
        }
        return resourceRefs;
    }
}
