public class nacos_0212 {

        @Override
        public McpServerIndexData getMcpServerById(String id) {
            if (!cacheEnabled) {
                LOGGER.debug("Cache disabled, querying directly from database for mcpId: {}", id);
                return getMcpServerByIdFromDatabase(id);
            }
            // Priority query cache
            McpServerIndexData cachedData = cacheIndex.getMcpServerById(id);
            if (cachedData != null) {
                LOGGER.debug("Cache hit for mcpId: {}", id);
                return cachedData;
            }
            // Cache miss, query database
            LOGGER.debug("Cache miss for mcpId: {}, querying database", id);
            McpServerIndexData dbData = getMcpServerByIdFromDatabase(id);
            if (dbData != null) {
                cacheIndex.updateIndex(dbData.getNamespaceId(), dbData.getId(), dbData.getId());
                LOGGER.debug("Updated cache for mcpId: {}", id);
            }
            return dbData;
        }
}
