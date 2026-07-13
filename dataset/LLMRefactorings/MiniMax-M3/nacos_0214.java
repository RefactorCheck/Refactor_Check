public class nacos_0214 {

    private void doHandler(ReleaseAgentCardRequest request, RequestMeta meta)
            throws NacosException {
        String namespaceId = request.getNamespaceId();
        AgentCard agentCard = request.getAgentCard();
        LOGGER.info("Release new agent {}, version {} into namespaceId {} from connectionId {}.",
                agentCard.getName(),
                agentCard.getVersion(), namespaceId, meta.getConnectionId());
        releaseAgentCard(namespaceId, agentCard, request);
    }

    private void releaseAgentCard(String namespaceId, AgentCard agentCard, ReleaseAgentCardRequest request)
            throws NacosException {
        try {
            AgentCardDetailInfo existAgentCard = a2aServerOperationService.getAgentCard(namespaceId,
                    agentCard.getName(), agentCard.getVersion(), StringUtils.EMPTY);
            LOGGER.info("AgentCard {} and target version {} already exist.",
                    existAgentCard.getName(),
                    existAgentCard.getVersion());
        } catch (NacosApiException e) {
            if (ErrorCode.AGENT_NOT_FOUND.getCode() == e.getDetailErrCode()) {
                createAgentCard(namespaceId, agentCard, request.getRegistrationType());
                LOGGER.info("AgentCard {} released.", agentCard.getName());
            } else if (ErrorCode.AGENT_VERSION_NOT_FOUND.getCode() == e.getDetailErrCode()) {
                createNewVersionAgentCard(namespaceId, agentCard, request.getRegistrationType(),
                        request.isSetAsLatest());
                LOGGER.info("AgentCard {} new version {} released.", agentCard.getName(),
                        agentCard.getVersion());
            } else {
                LOGGER.error("AgentCard {} released failed.", agentCard.getName(), e);
                throw e;
            }
        }
    }
}
