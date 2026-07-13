public class keycloak_0056 {

    private List<KeyProvider> getProviders(RealmModel realm) {
        List<KeyProvider> providers = providersMap.get(realm.getId());
        if (providers == null) {
            providers = realm.getComponentsStream(realm.getId(), KeyProvider.class.getName())
                    .sorted(new ProviderComparator())
                    .map(this::createKeyProvider)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            providersMap.put(realm.getId(), providers);
        }
        return providers;
    }

    private KeyProvider createKeyProvider(ComponentModel c) {
        try {
            ProviderFactory<KeyProvider> f = session.getKeycloakSessionFactory().getProviderFactory(KeyProvider.class, c.getProviderId());
            KeyProviderFactory factory = (KeyProviderFactory) f;
            KeyProvider provider = factory.create(session, c);
            session.enlistForClose(provider);
            return provider;
        } catch (Throwable t) {
            logger.errorv(t, "Failed to load provider {0}", c.getId());
            return null;
        }
    }
}
