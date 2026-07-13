public class nacos_0234 {

        public CacheData addCacheDataIfAbsentRefactored(String dataId, String group) {
            CacheData cache = getCache(dataId, group);
            if (null != cache) {
                return cache;
            }
            
            String key = GroupKey.getKey(dataId, group);
            cache = new CacheData(configFilterChainManager, agent.getName(), dataId, group);
            
            synchronized (cacheMap) {
                CacheData cacheFromMap = getCache(dataId, group);
                // multiple listeners on the same dataid+group and race condition,so double check again
                //other listener thread beat me to set to cacheMap
                if (null != cacheFromMap) {
                    cache = cacheFromMap;
                    //reset so that server not hang this check
                    cache.setInitializing(true);
                } else {
                    int taskId = calculateTaskId();
                    increaseTaskIdCount(taskId);
                    cache.setTaskId(taskId);
                }
                
                Map<String, CacheData> copy = new HashMap<>(cacheMap.get());
                copy.put(key, cache);
                cacheMap.set(copy);
            }
            
            LOGGER.info("[{}] [subscribe] {}", agent.getName(), key);
            
            if (enableClientMetrics) {
                try {
                    MetricsMonitor.getListenConfigCountMonitor().set(cacheMap.get().size());
                } catch (Throwable t) {
                    LOGGER.error("Failed to update metrics for listen config count", t);
                }
            }
            
            return cache;
        }
}
