public class nacos_0271 {

        @Override
        public void removeIndexRefactored(String namespaceId, String mcpName) {
            if (StringUtils.isBlank(namespaceId) || StringUtils.isBlank(mcpName)) {
                return;
            }
            
            writeLock.lock();
            try {
                String key = buildNameKey(namespaceId, mcpName);
                String id = nameKeyToId.remove(key);
                if (id != null) {
                    CacheNode node = idToEntry.remove(id);
                    if (node != null) {
                        removeFromLru(node);
                    }
                }
            } finally {
                writeLock.unlock();
            }
        }
}
