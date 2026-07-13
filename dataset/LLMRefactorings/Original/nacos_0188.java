public class nacos_0188 {

        public Set<String> initWatchMatchService(String completedPattern) throws NacosException {
            
            if (!matchedServiceKeysMap.containsKey(completedPattern)) {
                if (matchedServiceKeysMap.size() >= GlobalConfig.getMaxPatternCount()) {
                    Loggers.SRV_LOG.warn(
                        "FUZZY_WATCH: fuzzy watch pattern count is over limit ,pattern {} init fail,current count is {}",
                        completedPattern, matchedServiceKeysMap.size());
                    throw new NacosException(FUZZY_WATCH_PATTERN_OVER_LIMIT.getCode(),
                        FUZZY_WATCH_PATTERN_OVER_LIMIT.getMsg());
                }
                
                long matchBeginTime = System.currentTimeMillis();
                Set<Service> namespaceServices = ServiceManager.getInstance()
                    .getSingletons(getNamespaceFromPattern(completedPattern));
                Set<String> matchedServices =
                    matchedServiceKeysMap.computeIfAbsent(completedPattern, k -> new HashSet<>());
                boolean overMatchCount = false;
                for (Service service : namespaceServices) {
                    if (FuzzyGroupKeyPattern.matchPattern(completedPattern, service.getName(),
                        service.getGroup(),
                        service.getNamespace())) {
                        if (matchedServices.size() >= GlobalConfig.getMaxMatchedServiceCount()) {
                            
                            Loggers.SRV_LOG.warn(
                                "[fuzzy-watch] pattern matched service count is over limit , "
                                    + "other services will stop notify for pattern {} ,current count is {}",
                                completedPattern, matchedServices.size());
                            overMatchCount = true;
                            break;
                        }
                        matchedServices.add(
                            NamingUtils.getServiceKey(service.getNamespace(), service.getGroup(),
                                service.getName()));
                    }
                }
                Loggers.SRV_LOG.info(
                    "FUZZY_WATCH: pattern {} match {} services, overMatchCount={},cost {}ms",
                    completedPattern, matchedServices.size(), overMatchCount,
                    System.currentTimeMillis() - matchBeginTime);
                
            }
            
            return new HashSet(matchedServiceKeysMap.get(completedPattern));
        }
}
