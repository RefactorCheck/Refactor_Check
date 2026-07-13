public class nacos_0227 {

        public void deleteMcpServerRefactored(String namespaceId, String mcpName, String mcpServerId,
            String version)
            throws NacosException {
            mcpServerId = resolveMcpServerId(namespaceId, mcpName, mcpServerId);
            McpServerVersionInfo mcpServerVersionInfo =
                getMcpServerVersionInfo(namespaceId, mcpServerId);
            List<String> versionsNeedDelete = new ArrayList<>();
            if (StringUtils.isNotEmpty(version)) {
                versionsNeedDelete.add(version);
            } else {
                versionsNeedDelete = mcpServerVersionInfo.getVersionDetails().stream()
                    .map(ServerVersionDetail::getVersion)
                    .collect(Collectors.toList());
            }
            
            for (String versionNeedDelete : versionsNeedDelete) {
                toolOperationService.deleteMcpTool(namespaceId, mcpServerId, versionNeedDelete);
                resourceOperationService.deleteMcpResource(namespaceId, mcpServerId, versionNeedDelete);
                endpointOperationService.deleteMcpServerEndpointService(namespaceId,
                    mcpServerVersionInfo.getName() + "::" + versionNeedDelete);
                String serverSpecDataId =
                    McpConfigUtils.formatServerSpecInfoDataId(mcpServerId, versionNeedDelete);
                configOperationService.deleteConfig(serverSpecDataId, Constants.MCP_SERVER_GROUP,
                    namespaceId, null, null,
                    "nacos", null);
                String serverVersionDataId = McpConfigUtils.formatServerVersionInfoDataId(mcpServerId);
                configOperationService.deleteConfig(serverVersionDataId,
                    Constants.MCP_SERVER_VERSIONS_GROUP, namespaceId,
                    null, null, "nacos", null);
            }
            
            // Delete the relevant cache after a successful database operation
            invalidateCacheAfterDbOperation(namespaceId, mcpName, mcpServerId);
            AiResourceTraceService.logSuccess("mcp", mcpName, version,
                StringUtils.isNotEmpty(version) ? AiResourceTraceService.OP_DELETE_VERSION
                    : AiResourceTraceService.OP_DELETE_RESOURCE,
                VisibilityHelper.resolveCurrentIdentity(), VisibilityHelper.resolveClientIp());
        }
}
