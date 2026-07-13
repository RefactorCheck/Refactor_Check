public class nacos_0009 {

        public void executeConfigFuzzyListen() throws NacosException {
            
            // Obtain the current timestamp
            long now = System.currentTimeMillis();
            
            // Determine whether a full synchronization is needed
            boolean needAllSync =
                now - fuzzyListenLastAllSyncTime.get() >= FUZZY_LISTEN_ALL_SYNC_INTERNAL;
            
            List<ConfigFuzzyWatchContext> needSyncContexts = new ArrayList<>();
            // Iterate through all fuzzy listen contexts
            for (ConfigFuzzyWatchContext context : fuzzyListenContextMap.get().values()) {
                // Check if the context is consistent with the server
                if (context.isConsistentWithServer()) {
                    context.syncFuzzyWatchers();
                    // Skip if a full synchronization is not needed
                    if (!needAllSync) {
                        continue;
                    }
                }
                
                needSyncContexts.add(context);
            }
            
            // Execute fuzzy listen operation for addition
            doExecuteConfigFuzzyListen(needSyncContexts);
            
            // Update last all sync time if a full synchronization was performed
            if (needAllSync) {
                fuzzyListenLastAllSyncTime.set(now);
            }
        }
}
