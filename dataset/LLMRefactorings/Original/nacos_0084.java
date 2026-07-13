public class nacos_0084 {

        private void injectMcpDescriptionsAndEndpoint(String namespaceId, String mcpServerId,
            McpServerStorageInfo serverSpecification, McpToolSpecification toolSpecification,
            McpResourceSpecification resourceSpecification, McpEndpointSpec endpointSpecification,
            boolean overrideExisting, McpServerStorageInfo oldSpecification) throws NacosException {
            serverSpecification.setCapabilities(new LinkedList<>());
            boolean hasToolSpec = toolSpecification != null;
            boolean hasTools = hasToolSpec && toolSpecification.getTools() != null;
            boolean hasSecuritySchemes = hasToolSpec && toolSpecification.getSecuritySchemes() != null;
            boolean hasEncryptedData = hasToolSpec && toolSpecification.getEncryptData() != null;
            boolean shouldCreateToolConfig =
                hasToolSpec && (hasTools || hasSecuritySchemes || hasEncryptedData);
            if (shouldCreateToolConfig) {
                toolOperationService.refreshMcpTool(namespaceId, serverSpecification,
                    toolSpecification);
                serverSpecification.getCapabilities().add(McpCapability.TOOL);
                String version = serverSpecification.getVersionDetail().getVersion();
                String toolSpecDataId = McpConfigUtils.formatServerToolSpecDataId(mcpServerId, version);
                serverSpecification.setToolsDescriptionRef(toolSpecDataId);
            }
            boolean hasResourceSpec = hasResourceSpecification(resourceSpecification);
            if (hasResourceSpec) {
                resourceOperationService.refreshMcpResource(namespaceId, serverSpecification,
                    resourceSpecification);
                serverSpecification.getCapabilities().add(McpCapability.RESOURCE);
                String version = serverSpecification.getVersionDetail().getVersion();
                String resourceSpecDataId =
                    McpConfigUtils.formatServerResourceSpecDataId(mcpServerId, version);
                serverSpecification.setResourceDescriptionRef(resourceSpecDataId);
            } else if (Objects.nonNull(oldSpecification)
                && StringUtils.isNotBlank(oldSpecification.getResourceDescriptionRef())) {
                resourceOperationService.deleteMcpResource(namespaceId, mcpServerId,
                    serverSpecification.getVersionDetail().getVersion());
                serverSpecification.setResourceDescriptionRef(null);
            }
            if (null != endpointSpecification) {
                Service service = endpointOperationService.createMcpServerEndpointServiceIfNecessary(
                    namespaceId,
                    serverSpecification.getName(), serverSpecification.getVersionDetail().getVersion(),
                    endpointSpecification, overrideExisting);
                String transportProtocol =
                    endpointSpecification.getData().get(Constants.MCP_BACKEND_INSTANCE_PROTOCOL_KEY);
                McpServiceRef serviceRef = new McpServiceRef();
                serviceRef.setNamespaceId(service.getNamespace());
                serviceRef.setGroupName(service.getGroup());
                serviceRef.setServiceName(service.getName());
                serviceRef.setTransportProtocol(transportProtocol);
                serverSpecification.getRemoteServerConfig().setServiceRef(serviceRef);
            }
        }
}
