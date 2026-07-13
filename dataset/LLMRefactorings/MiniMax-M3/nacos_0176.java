public class nacos_0176 {

        private static List<AgentInterface> normalizeSupportedInterfaces(AgentCard agentCard) {
            List<AgentInterface> result = new ArrayList<>();
            if (null != agentCard.getSupportedInterfaces()) {
                addNormalizedInterfaces(result, agentCard.getSupportedInterfaces(), null);
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
                    addNormalizedInterfaces(result, agentCard.getAdditionalInterfaces(), agentCard);
                }
            }
            return result;
        }

        private static void addNormalizedInterfaces(List<AgentInterface> result, List<AgentInterface> interfaces, AgentCard agentCard) {
            if (null == interfaces) {
                return;
            }
            for (AgentInterface each : interfaces) {
                AgentInterface normalized = normalizeAgentInterface(each);
                if (null != agentCard) {
                    if (StringUtils.isEmpty(normalized.getProtocolVersion())) {
                        normalized.setProtocolVersion(agentCard.getProtocolVersion());
                    }
                    if (StringUtils.isEmpty(normalized.getProtocolBinding())) {
                        normalized.setProtocolBinding(agentCard.getPreferredTransport());
                    }
                }
                if (isValidAgentInterface(normalized)) {
                    result.add(normalized);
                }
            }
        }
}
