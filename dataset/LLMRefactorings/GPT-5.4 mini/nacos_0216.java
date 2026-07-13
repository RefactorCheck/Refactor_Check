public class nacos_0216 {

        private static byte[] buildMainContentRefactored(AgentSpec agentSpec, long uniformId) {
            AgentSpecMainConfig mainConfig = new AgentSpecMainConfig();
            mainConfig.setName(agentSpec.getName());
            mainConfig.setDescription(agentSpec.getDescription());
            mainConfig.setContent(agentSpec.getContent());
            mainConfig.setUniformId(uniformId);
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
            mainConfig.setResources(resourceRefs);
            return JacksonUtils.toJson(mainConfig).getBytes(StandardCharsets.UTF_8);
        }
}
