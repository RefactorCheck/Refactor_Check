public class arthas_0076 {

        @Override
        public Map<String, AgentClusterInfo> agentInfo(String appName) {
            CaffeineCache caffeineCache = (CaffeineCache) cache;
            com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
    
            ConcurrentMap<String, AgentClusterInfo> map = (ConcurrentMap<String, AgentClusterInfo>) (ConcurrentMap<?, ?>) nativeCache
                    .asMap();
    
            return filterByPrefix(map, appName + "_");
        }
    
        private Map<String, AgentClusterInfo> filterByPrefix(ConcurrentMap<String, AgentClusterInfo> map, String prefix) {
            Map<String, AgentClusterInfo> result = new HashMap<String, AgentClusterInfo>();
            for (Entry<String, AgentClusterInfo> entry : map.entrySet()) {
                String agentId = entry.getKey();
                if (agentId.startsWith(prefix)) {
                    result.put(agentId, entry.getValue());
                }
            }
            return result;
        }
}
