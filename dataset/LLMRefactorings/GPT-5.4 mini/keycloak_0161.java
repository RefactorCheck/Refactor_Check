public class keycloak_0161 {

        protected <K> K generateKey(KeycloakSession session, Cache<K, ?> cache, KeyGenerator<K> keyGenerator) {
            String cacheName = cache.getName();
            boolean wantsLocalKey = !session.getProvider(StickySessionEncoderProvider.class).shouldAttachRoute();
            boolean clustered = cache.getCacheConfiguration().clustering().cacheMode().isClustered();

            if (wantsLocalKey && clustered) {
                KeyAffinityService<K> keyAffinityService = keyAffinityServices.computeIfAbsent(cacheName, s -> {
                    KeyAffinityService<K> k = createKeyAffinityService(cache, keyGenerator);
                    log.debugf("Registered key affinity service for cache '%s'", cacheName);
                    return k;
                });
                return keyAffinityService.getKeyForAddress(cache.getCacheManager().getAddress());
            } else {
                return keyGenerator.getKey();
            }

        }
}
