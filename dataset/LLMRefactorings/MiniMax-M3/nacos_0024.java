public class nacos_0024 {

    public Page<AgentCardVersionInfo> listAgents(String namespaceId, String agentName,
        String search, int pageNo,
        int pageSize) throws NacosException {
        
        String normalizedAgentName = agentName == null ? StringUtils.EMPTY : agentName;
        String dataId;
        if (StringUtils.isEmpty(normalizedAgentName)
            || Constants.A2A.SEARCH_BLUR.equalsIgnoreCase(search)) {
            search = Constants.A2A.SEARCH_BLUR;
            dataId = Constants.ALL_PATTERN + agentIdCodecHolder.encodeForSearch(normalizedAgentName)
                + Constants.ALL_PATTERN;
        } else {
            search = Constants.A2A.SEARCH_ACCURATE;
            dataId = agentIdCodecHolder.encode(normalizedAgentName);
        }
        
        Page<ConfigInfo> configInfoPage =
            configDetailService.findConfigInfoPage(search, pageNo, pageSize, dataId,
                AGENT_GROUP, namespaceId, null);
        
        return toAgentCardPage(configInfoPage, pageNo, pageSize);
    }
    
    private Page<AgentCardVersionInfo> toAgentCardPage(Page<ConfigInfo> configInfoPage, int pageNo, int pageSize) {
        List<AgentCardVersionInfo> versionInfos = configInfoPage.getPageItems().stream()
            .map(configInfo -> JacksonUtils.toObj(configInfo.getContent(),
                AgentCardVersionInfo.class))
            .toList();
        
        Page<AgentCardVersionInfo> result = new Page<>();
        result.setPageItems(versionInfos);
        result.setTotalCount(configInfoPage.getTotalCount());
        result.setPagesAvailable(
            (int) Math.ceil((double) configInfoPage.getTotalCount() / (double) pageSize));
        result.setPageNumber(pageNo);
        
        return result;
    }
}
