public class nacos_0274 {

        private void createDraftWithAgentSpec(String namespaceId, AgentSpec agentSpec, String version,
            AiResource existedMeta, boolean isNew) throws NacosException {
            String agentSpecName = agentSpec.getName();
            long uniformId = System.currentTimeMillis();
            String currentUser = VisibilityHelper.resolveCurrentIdentity();
            String author = StringUtils.isBlank(currentUser) ? DEFAULT_AUTHOR : currentUser;
            
            // 1) write storage for draft version (main config + resources persisted concurrently)
            saveAgentSpecFilesConcurrently(namespaceId, agentSpec, version, uniformId);
            
            // 2) insert draft version row
            resourceManager.insertVersionRow(namespaceId, agentSpecName, RESOURCE_TYPE_AGENTSPEC,
                author,
                AiResourceConstants.VERSION_STATUS_DRAFT, version, agentSpec.getDescription(),
                buildStorageJson(namespaceId, agentSpecName, version));
            
            // 3) create or update meta for editingVersion
            resourceManager.initOrUpdateMetaForDraft(namespaceId, agentSpecName,
                RESOURCE_TYPE_AGENTSPEC,
                agentSpec.getDescription(), agentSpec.getBizTags(), version, existedMeta, isNew);
        }
}
