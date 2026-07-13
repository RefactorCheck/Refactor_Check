public class nacos_0271 {

        @Override
        public void removeIndex(String namespaceId, String mcpName) {
            if (StringUtils.isBlank(namespaceId) || StringUtils.isBlank(mcpName)) {
                return;
            }
            
            writeLock.lock();
            try {
                doRemoveIndex(namespaceId, mcpName);
            } finally {
                writeLock.unlock();
            }
        }
        
        private void doRemoveIndex(String namespaceId, String mcpName) {
            String key = buildNameKey(namespaceId, mcpName);
            String id = nameKeyToId.remove(key);
            if (id != null) {
                CacheNode node = idToEntry.remove(id);
                if (node != null) {
                    removeFromLru(node);
                }
            }
        }
}
