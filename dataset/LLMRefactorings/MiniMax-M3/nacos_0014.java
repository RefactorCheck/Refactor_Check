public class nacos_0014 {

    public boolean syncServiceContext(Service changedService, String changedType) {
        
        boolean needNotify = false;
        if (!changedType.equals(ADD_SERVICE) && !changedType.equals(DELETE_SERVICE)) {
            return false;
        }
        
        String serviceKey =
            NamingUtils.getServiceKey(changedService.getNamespace(), changedService.getGroup(),
                changedService.getName());
        Loggers.SRV_LOG.warn("[fuzzy-watch] service change matched,service key {},changed type {} ",
            serviceKey,
            changedType);
        
        Iterator<Map.Entry<String, Set<String>>> iterator =
            matchedServiceKeysMap.entrySet().iterator();
        boolean tryAdd = changedType.equals(ADD_SERVICE);
        boolean tryRemove = changedType.equals(DELETE_SERVICE);
        while (iterator.hasNext()) {
            Map.Entry<String, Set<String>> next = iterator.next();
            if (FuzzyGroupKeyPattern.matchPattern(next.getKey(), changedService.getName(),
                changedService.getGroup(),
                changedService.getNamespace())) {
                if (processMatchedEntry(next, serviceKey, tryAdd, tryRemove)) {
                    needNotify = true;
                }
            }
        }
        return needNotify;
    }
    
    private boolean processMatchedEntry(Map.Entry<String, Set<String>> entry, String serviceKey,
        boolean tryAdd, boolean tryRemove) {
        Set<String> matchedServiceKeys = entry.getValue();
        boolean reachToUpLimit = reachToUpLimit(matchedServiceKeys.size());
        boolean containsAlready = matchedServiceKeys.contains(serviceKey);
        
        if (tryAdd && !containsAlready && reachToUpLimit) {
            Loggers.SRV_LOG.warn(
                "[fuzzy-watch] pattern matched service count is over limit , "
                    + "current service will be ignore for pattern {} ,current count is {}",
                entry.getKey(),
                matchedServiceKeys.size());
            return false;
        }
        
        boolean notified = false;
        if (tryAdd && !containsAlready && matchedServiceKeys.add(serviceKey)) {
            Loggers.SRV_LOG.info(
                "[fuzzy-watch] pattern {} matched service keys count changed to {}",
                entry.getKey(), matchedServiceKeys.size());
            notified = true;
        }
        if (tryRemove && containsAlready && matchedServiceKeys.remove(serviceKey)) {
            Loggers.SRV_LOG.info(
                "[fuzzy-watch]  pattern {} matched service keys count changed to {}",
                entry.getKey(), matchedServiceKeys.size());
            notified = true;
            if (reachToUpLimit) {
                makeupMatchedGroupKeys(entry.getKey());
            }
        }
        return notified;
    }
}
