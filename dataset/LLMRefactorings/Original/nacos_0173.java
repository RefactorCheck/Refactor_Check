public class nacos_0173 {

        private McpServerRemoteServiceConfig generateRemoteServiceConfig(List<Remote> remotes) {
            if (CollectionUtils.isEmpty(remotes)) {
                return null;
            }
            
            McpServerRemoteServiceConfig remoteConfig = new McpServerRemoteServiceConfig();
            List<FrontEndpointConfig> endpoints = new ArrayList<>();
            
            for (Remote remote : remotes) {
                String url = remote.getUrl().trim();
                try {
                    UrlComponents components = parseUrlComponents(url);
                    boolean isHttps = "https".equalsIgnoreCase(components.getScheme());
                    int effectivePort =
                        (components.getPort() > 0) ? components.getPort() : (isHttps ? 443 : 80);
                    String endpointData = components.getHost() + ":" + effectivePort;
                    FrontEndpointConfig cfg = new FrontEndpointConfig();
                    cfg.setEndpointData(endpointData);
                    cfg.setPath(
                        StringUtils.isNotBlank(components.getPath()) ? components.getPath() : "/");
                    cfg.setType(remote.getType());
                    cfg.setProtocol(components.getScheme());
                    cfg.setEndpointType(AiConstants.Mcp.MCP_FRONT_ENDPOINT_TYPE_TO_BACK);
                    cfg.setHeaders(remote.getHeaders());
                    endpoints.add(cfg);
                    
                    // Use first remote's path as export path
                    if (remoteConfig.getExportPath() == null) {
                        remoteConfig
                            .setExportPath(components.getPath() != null ? components.getPath() : "/");
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("Invalid URL: " + url, e);
                }
            }
            
            remoteConfig.setFrontEndpointConfigList(endpoints);
            return remoteConfig;
        }
}
