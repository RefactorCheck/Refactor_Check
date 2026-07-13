public class nacos_0015 {

        private void injectEndpoint(AgentCardDetailInfo agentCard, String namespaceId)
            throws NacosApiException {
            String serviceName =
                agentIdCodecHolder.encode(agentCard.getName()) + "::" + agentCard.getVersion();
            Service service =
                Service.newService(namespaceId, Constants.A2A.AGENT_ENDPOINT_GROUP, serviceName);
            ServiceInfo serviceInfo = serviceStorage.getData(service);
            if (serviceInfo.getHosts().isEmpty()) {
                return;
            }
            String fallbackProtocolVersion = resolveEndpointFallbackProtocolVersion(agentCard);
            List<AgentInterface> allAgentEndpoints =
                serviceInfo.getHosts().stream().map(AgentCardUtil::buildAgentInterface)
                    .toList();
            normalizeAgentEndpoints(allAgentEndpoints, fallbackProtocolVersion, agentCard);
            agentCard.setSupportedInterfaces(allAgentEndpoints);
            agentCard.setAdditionalInterfaces(allAgentEndpoints);
            List<AgentInterface> matchTransportEndpoints = allAgentEndpoints.stream()
                .filter(
                    agentInterface -> StringUtils.equalsIgnoreCase(agentInterface.getProtocolBinding(),
                        agentCard.getPreferredTransport()))
                .toList();
            AgentInterface randomPreferredTransportEndpoint = randomOne(
                matchTransportEndpoints.isEmpty() ? allAgentEndpoints : matchTransportEndpoints);
            agentCard.setUrl(randomPreferredTransportEndpoint.getUrl());
            agentCard.setPreferredTransport(randomPreferredTransportEndpoint.getProtocolBinding());
            agentCard.setProtocolVersion(randomPreferredTransportEndpoint.getProtocolVersion());
        }

        private void normalizeAgentEndpoints(List<AgentInterface> endpoints,
            String fallbackProtocolVersion, AgentCardDetailInfo agentCard)
            throws NacosApiException {
            for (AgentInterface each : endpoints) {
                if (StringUtils.isEmpty(each.getProtocolBinding())
                    && StringUtils.isNotEmpty(each.getTransport())) {
                    each.setProtocolBinding(each.getTransport());
                }
                if (StringUtils.isEmpty(each.getTransport())
                    && StringUtils.isNotEmpty(each.getProtocolBinding())) {
                    each.setTransport(each.getProtocolBinding());
                }
                if (StringUtils.isEmpty(each.getProtocolVersion())) {
                    each.setProtocolVersion(fallbackProtocolVersion);
                }
                if (StringUtils.isEmpty(each.getProtocolVersion())) {
                    throw new NacosApiException(NacosException.INVALID_PARAM,
                        ErrorCode.PARAMETER_MISSING,
                        String.format(
                            "Agent endpoint protocolVersion is missing for agent %s version %s. "
                                + "Please set endpoint protocolVersion or card protocolVersion.",
                            agentCard.getName(), agentCard.getVersion()));
                }
            }
        }
}
