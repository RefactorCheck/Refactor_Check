protected static void removeAllLocalUserLoginFailuresEvent(String realmId) {
            log.tracef("removeAllLocalUserLoginFailuresEvent(%s)%s", realmId, getShortStackTrace());
    
            FuturesHelper futures = new FuturesHelper();
    
            Cache<LoginFailureKey, SessionEntityWrapper<LoginFailureEntity>> localCache = CacheDecorators.localCache(loginFailuresTx.getCache());
    
            // Go through local cache data only
            // entries from other nodes will be removed by each instance receiving the event
            localCache
                    .entrySet()
                    .stream()
                    .filter(SessionWrapperPredicate.create(realmId))
                    .map(Mappers.loginFailureId())
                    .forEach(loginFailureKey -> {
                        // Remove loginFailure from remoteCache too. Use removeAsync for better perf
                        Future<?> future = removeKeyFromCache(localCache, loginFailureKey);
                        futures.addTask(future);
                    });
    
            futures.waitForAllToFinish();
    
            log.debugf("Removed %d login failures in realm %s", futures.size(), realmId);
        }
