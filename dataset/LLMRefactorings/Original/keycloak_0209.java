public class keycloak_0209 {

        @Override
        public Stream<FederatedIdentityModel> getFederatedIdentitiesStream(RealmModel realm, UserModel user) {
            logger.tracev("getFederatedIdentities: {0}", user.getUsername());
    
            String cacheKey = getFederatedIdentityLinksCacheKey(user.getId());
            if (realmInvalidations.contains(realm.getId()) || invalidations.contains(user.getId()) || invalidations.contains(cacheKey)) {
                return getDelegate().getFederatedIdentitiesStream(realm, user);
            }
    
            CachedFederatedIdentityLinks cachedLinks = cache.get(cacheKey, CachedFederatedIdentityLinks.class);
    
            if (cachedLinks == null) {
                long loaded = cache.getCurrentRevision(cacheKey);
                Set<FederatedIdentityModel> federatedIdentities = getDelegate().getFederatedIdentitiesStream(realm, user)
                        .collect(Collectors.toSet());
                cachedLinks = new CachedFederatedIdentityLinks(loaded, cacheKey, realm, federatedIdentities);
                cache.addRevisioned(cachedLinks, startupRevision); // this is Keycloak's internal store, cache indefinitely
                return federatedIdentities.stream();
            } else {
                return cachedLinks.getFederatedIdentities().stream();
            }
        }
}
