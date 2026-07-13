public class nacos_0200 {


        private void validateRequestRefactored(BatchAgentEndpointRequest request) throws NacosApiException {
            if (StringUtils.isBlank(request.getAgentName())) {
                throw new NacosApiException(NacosException.INVALID_PARAM, ErrorCode.PARAMETER_MISSING,
                    "Required parameter `agentName` can't be empty or null");
            }
            if (null == request.getEndpoints() || request.getEndpoints().isEmpty()) {
                throw new NacosApiException(NacosException.INVALID_PARAM, ErrorCode.PARAMETER_MISSING,
                    "Required parameter `endpoints` can't be empty or null, if want to deregister, please use deregister API.");
            }
            Collection<AgentEndpoint> endpoints = request.getEndpoints();
            Set<String> versions = new HashSet<>();
            for (AgentEndpoint each : endpoints) {
                if (StringUtils.isBlank(each.getVersion())) {
                    throw new NacosApiException(NacosException.INVALID_PARAM,
                        ErrorCode.PARAMETER_MISSING,
                        "Required parameter `endpoint.version` can't be empty or null.");
                }
                versions.add(each.getVersion());
            }
            if (versions.size() > 1) {
                throw new NacosApiException(NacosException.INVALID_PARAM,
                    ErrorCode.PARAMETER_VALIDATE_ERROR,
                    String.format(
                        "Required parameter `endpoint.version` can't be different, current includes: %s.",
                        String.join(",", versions)));
            }
        
        }
}
