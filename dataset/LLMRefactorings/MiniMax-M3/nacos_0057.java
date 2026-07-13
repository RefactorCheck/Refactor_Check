public class nacos_0057 {

    public void registerAgent(AgentCard agentCard, String namespaceId, String registrationType)
        throws NacosException {
        try {
            registerAgentInfo(agentCard, namespaceId, registrationType);

            long startOperationTime = System.currentTimeMillis();
            ConfigForm configFormVersion = registerAgentVersionInfo(agentCard, namespaceId, registrationType);

            syncEffectService.toSync(configFormVersion, startOperationTime);
            logAgentRegistration(agentCard);
        } catch (ConfigAlreadyExistsException e) {
            throw new NacosApiException(NacosException.CONFLICT, ErrorCode.RESOURCE_CONFLICT,
                String.format("AgentCard name %s already exist", agentCard.getName()));
        }
    }

    private void registerAgentInfo(AgentCard agentCard, String namespaceId, String registrationType) {
        AgentCardVersionInfo agentCardVersionInfo =
            AgentCardUtil.buildAgentCardVersionInfo(agentCard, registrationType, true);
        ConfigForm configForm = transferVersionInfoToConfigForm(agentCardVersionInfo, namespaceId);
        ConfigRequestInfo versionConfigRequest = new ConfigRequestInfo();
        versionConfigRequest.setUpdateForExist(Boolean.FALSE);
        configOperationService.publishConfig(configForm, versionConfigRequest, null);
    }

    private ConfigForm registerAgentVersionInfo(AgentCard agentCard, String namespaceId, String registrationType) {
        AgentCardDetailInfo agentCardDetailInfo =
            AgentCardUtil.buildAgentCardDetailInfo(agentCard, registrationType);
        ConfigForm configFormVersion = transferAgentInfoToConfigForm(agentCardDetailInfo, namespaceId);
        ConfigRequestInfo agentCardConfigRequest = new ConfigRequestInfo();
        agentCardConfigRequest.setUpdateForExist(Boolean.FALSE);
        configOperationService.publishConfig(configFormVersion, agentCardConfigRequest, null);
        return configFormVersion;
    }

    private void logAgentRegistration(AgentCard agentCard) {
        AiResourceTraceService.logSuccess("a2a", agentCard.getName(), agentCard.getVersion(),
            AiResourceTraceService.OP_CREATE_DRAFT, VisibilityHelper.resolveCurrentIdentity(),
            VisibilityHelper.resolveClientIp());
    }
}
