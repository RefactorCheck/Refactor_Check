public class keycloak_0209 {

        @Override
        public Stream<FederatedIdentityModel> getFederatedIdentitiesStream(RealmModel realm, UserModel user) {
            logger.tracev("getFederatedIdentities: {0}", user.getUsername());

            String federatedIdentityLinksCacheKey = getFederatedIdentityLinksCacheKey(user.getId());
            if (realmInvalidations.contains(realm.getId()) || invalidations.contains(user.getId()) || invalidations.contains(federatedIdentityLinksCacheKey)) {
                return getDelegate().getFederatedIdentitiesStream(realm, user);
            }

            CachedFederatedIdentityLinks cachedFederatedIdentityLinks = cache.get(federatedIdentityLinksCacheKey, CachedFederatedIdentityLinks.class);

            if (cachedFederatedIdentityLinks == null) {
                long currentRevision = cache.getCurrentRevision(federatedIdentityLinksCacheKey);
                Set<FederatedIdentityModel> federatedIdentities = getDelegate().getFederatedIdentitiesStream(realm, user)
                        .collect(Collectors.toSet());
                cachedFederatedIdentityLinks = new CachedFederatedIdentityLinks(currentRevision, federatedIdentityLinksCacheKey, realm, federatedIdentities);
                cache.addRevisioned(cachedFederatedIdentityLinks, startupRevision); // this is Keycloak's internal store, cache indefinitely
                return federatedIdentities.stream();
            } else {
                return cachedFederatedIdentityLinks.getFederatedIdentities().stream();
            }
        }
}
