public class nacos_0054 {


        public static Instance transferToInstanceRefactored(AgentEndpoint endpoint) throws NacosApiException {
            Instance instance = new Instance();
            instance.setIp(endpoint.getAddress());
            instance.setPort(endpoint.getPort());
            String path =
                StringUtils.isBlank(endpoint.getPath()) ? StringUtils.EMPTY : endpoint.getPath();
            String protocol = StringUtils.isBlank(endpoint.getProtocol()) ? StringUtils.EMPTY
                : endpoint.getProtocol();
            String query =
                StringUtils.isBlank(endpoint.getQuery()) ? StringUtils.EMPTY : endpoint.getQuery();
            String protocolVersion =
                StringUtils.isBlank(endpoint.getProtocolVersion()) ? StringUtils.EMPTY
                    : endpoint.getProtocolVersion();
            String tenant =
                StringUtils.isBlank(endpoint.getTenant()) ? StringUtils.EMPTY : endpoint.getTenant();
            Map<String, String> metadata = Map.of(Constants.A2A.AGENT_ENDPOINT_PATH_KEY, path,
                Constants.A2A.AGENT_ENDPOINT_TRANSPORT_KEY, endpoint.getTransport(),
                Constants.A2A.NACOS_AGENT_ENDPOINT_SUPPORT_TLS, String.valueOf(endpoint.isSupportTls()),
                Constants.A2A.NACOS_AGENT_ENDPOINT_PROTOCOL_KEY, protocol,
                Constants.A2A.NACOS_AGENT_ENDPOINT_QUERY_KEY,
                query, Constants.A2A.NACOS_AGENT_ENDPOINT_PROTOCOL_VERSION_KEY, protocolVersion,
                Constants.A2A.NACOS_AGENT_ENDPOINT_TENANT_KEY, tenant);
            instance.setMetadata(metadata);
            instance.validate();
            return instance;
        
        }
}
