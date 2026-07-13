public class nacos_0176 {

        private static List<AgentInterface> normalizeSupportedInterfacesRefactored(AgentCard agentCard) {
            List<AgentInterface> result = new ArrayList<>();
            if (null != agentCard.getSupportedInterfaces()) {
                for (AgentInterface each : agentCard.getSupportedInterfaces()) {
                    AgentInterface normalized = normalizeAgentInterface(each);
                    if (isValidAgentInterface(normalized)) {
                        result.add(normalized);
                    }
                }
            }
            if (!result.isEmpty()) {
                return result;
            }
            if (isLegacyAgentCard(agentCard)) {
                AgentInterface preferred = new AgentInterface();
                preferred.setUrl(agentCard.getUrl());
                preferred.setProtocolBinding(agentCard.getPreferredTransport());
                preferred.setProtocolVersion(agentCard.getProtocolVersion());
                preferred.setTransport(agentCard.getPreferredTransport());
                result.add(preferred);
                if (null != agentCard.getAdditionalInterfaces()) {
                    for (AgentInterface each : agentCard.getAdditionalInterfaces()) {
                        AgentInterface normalized = normalizeAgentInterface(each);
                        if (StringUtils.isEmpty(normalized.getProtocolVersion())) {
                            normalized.setProtocolVersion(agentCard.getProtocolVersion());
                        }
                        if (StringUtils.isEmpty(normalized.getProtocolBinding())) {
                            normalized.setProtocolBinding(agentCard.getPreferredTransport());
                        }
                        if (isValidAgentInterface(normalized)) {
                            result.add(normalized);
                        }
                    }
                }
            }
            return result;
        }
}
