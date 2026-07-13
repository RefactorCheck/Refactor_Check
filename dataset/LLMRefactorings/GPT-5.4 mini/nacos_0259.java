public class nacos_0259 {

            @Override
            public void runRefactored() {
                Map<String, FailoverData> domMap = new HashMap<>(200);
                
                try {
                    File cacheDir = new File(failoverDir);
                    DiskCache.createFileIfAbsent(cacheDir, true);
                    
                    File[] files = cacheDir.listFiles();
                    if (files == null) {
                        return;
                    }
                    
                    for (File file : files) {
                        if (!file.isFile()) {
                            continue;
                        }
                        
                        if (file.getName().equals(UtilAndComs.FAILOVER_SWITCH)) {
                            continue;
                        }
                        
                        for (Map.Entry<String, ServiceInfo> entry : DiskCache
                            .parseServiceInfoFromCache(file).entrySet()) {
                            domMap.put(entry.getKey(),
                                NamingFailoverData.newNamingFailoverData(entry.getValue()));
                        }
                    }
                } catch (Exception e) {
                    NAMING_LOGGER.error("[NA] failed to read cache file", e);
                }
                
                if (domMap.size() > 0) {
                    serviceMap = domMap;
                }
            }
}
