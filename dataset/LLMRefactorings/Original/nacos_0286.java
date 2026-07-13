public class nacos_0286 {

        private void invalidateCacheAfterDbUpdateOperation(String namespaceId, String oldMcpName,
            String newMcpName,
            String mcpServerId) {
            try {
                if (StringUtils.isNotEmpty(oldMcpName) && !oldMcpName.equals(newMcpName)) {
                    mcpServerIndex.removeMcpServerByName(namespaceId, oldMcpName);
                }
                if (StringUtils.isNotEmpty(newMcpName)) {
                    mcpServerIndex.removeMcpServerByName(namespaceId, newMcpName);
                }
                if (StringUtils.isNotEmpty(mcpServerId)) {
                    mcpServerIndex.removeMcpServerById(mcpServerId);
                }
                LOGGER.debug(
                    "Cache invalidated after updateMcpServer: namespaceId={}, oldName={}, newName={}, id={}",
                    namespaceId, oldMcpName, newMcpName, mcpServerId);
            } catch (Exception e) {
                LOGGER.warn(
                    "Failed to invalidate cache after updateMcpServer: namespaceId={}, oldName={}, newName={}, id={}, error={}",
                    namespaceId, oldMcpName, newMcpName, mcpServerId, e.getMessage());
            }
        }
}
