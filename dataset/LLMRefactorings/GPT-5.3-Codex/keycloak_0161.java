protected <K> K generateKey(KeycloakSession session, Cache<K, ?> cache, KeyGenerator<K> keyGenerator) {
    
            // "wantsLocalKey" is true if route is not attached to the sticky session cookie. Without attached route, We want the key, which will be "owned" by this node.
            // This is needed due the fact that external loadbalancer will attach route corresponding to our node, which will be the owner of the particular key, hence we
            // will be able to lookup key locally.
            boolean wantsLocalKey = !session.getProvider(StickySessionEncoderProvider.class).shouldAttachRoute();
    
            if (wantsLocalKey && cache.getCacheConfiguration().clustering().cacheMode().isClustered()) {
                KeyAffinityService<K> keyAffinityService = keyAffinityServices.computeIfAbsent((cache.getName()), s -> {
                    KeyAffinityService<K> k = createKeyAffinityService(cache, keyGenerator);
                    log.debugf("Registered key affinity service for cache '%s'", (cache.getName()));
                    return k;
                });
                return keyAffinityService.getKeyForAddress(cache.getCacheManager().getAddress());
            } else {
                return keyGenerator.getKey();
            }
    
        }
