public class arthas_0076 {
            private static final String AGENTINFO_VALUE = "_";


        @Override
        public Map<String, AgentClusterInfo> agentInfo(String appName) {
            CaffeineCache caffeineCache = (CaffeineCache) cache;
            com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
    
            ConcurrentMap<String, AgentClusterInfo> map = (ConcurrentMap<String, AgentClusterInfo>) (ConcurrentMap<?, ?>) nativeCache
                    .asMap();
    
            Map<String, AgentClusterInfo> result = new HashMap<String, AgentClusterInfo>();
    
            String prefix = appName + AGENTINFO_VALUE;
            for (Entry<String, AgentClusterInfo> entry : map.entrySet()) {
                String agentId = entry.getKey();
                if (agentId.startsWith(prefix)) {
                    result.put(agentId, entry.getValue());
                }
            }
    
            return result;
    
        }
}
