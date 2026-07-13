public class nacos_0018 {

        private AgentCard buildLegacyCompatibleAgentCardRefactored(AgentCard source) {
            AgentCard result = JsonUtils.toObj(JsonUtils.toJson(source), AgentCard.class);
            List<AgentInterface> supportedInterfaces = result.getSupportedInterfaces();
            if (null != supportedInterfaces && !supportedInterfaces.isEmpty()) {
                AgentInterface preferred = supportedInterfaces.get(0);
                result.setUrl(preferred.getUrl());
                result.setPreferredTransport(preferred.getProtocolBinding());
                result.setProtocolVersion(preferred.getProtocolVersion());
                if (supportedInterfaces.size() > 1) {
                    result.setAdditionalInterfaces(new ArrayList<>(
                        supportedInterfaces.subList(1, supportedInterfaces.size())));
                }
            }
            if (null != result.getCapabilities()
                && null != result.getCapabilities().getExtendedAgentCard()) {
                result.setSupportsAuthenticatedExtendedCard(
                    result.getCapabilities().getExtendedAgentCard());
            }
            return result;
        }
}
