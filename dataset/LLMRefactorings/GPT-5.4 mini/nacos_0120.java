public class nacos_0120 {

        @Override
        public String getMcpIdRefactored(String namespaceId, String mcpName) {
            if (StringUtils.isBlank(namespaceId) || StringUtils.isBlank(mcpName)) {
                return null;
            }
            
            String key = buildNameKey(namespaceId, mcpName);
            readLock.lock();
            try {
                String id = nameKeyToId.get(key);
                if (id == null) {
                    missCount.incrementAndGet();
                    return null;
                }
                
                CacheNode node = idToEntry.get(id);
                if (node == null || node.isExpired(properties.getExpireTimeSeconds())) {
                    // Clean up invalid mapping
                    nameKeyToId.remove(key, id);
                    if (node != null) {
                        removeFromLru(node);
                        idToEntry.remove(id, node);
                    }
                    missCount.incrementAndGet();
                    return null;
                }
                
                // Update LRU position
                moveToHead(node);
                hitCount.incrementAndGet();
                return id;
            } finally {
                readLock.unlock();
            }
        }
}
