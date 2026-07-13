public class keycloak_0134 {

    @Override
    protected void doRunCacheCommand(KeycloakSession session, Cache<String, SessionEntityWrapper> cache) {
        String realmName = getArg(1);
        int count = getIntArg(2);
        int batchCount = getIntArg(3);

        BatchTaskRunner.runInBatches(0, count, batchCount, session.getKeycloakSessionFactory(), (KeycloakSession batchSession, int firstInIteration, int countInIteration) -> {
            for (int i=0 ; i<countInIteration ; i++) {
                createAndCacheSession(cache, realmName);
            }

            log.infof("Created '%d' sessions started from offset '%d'", countInIteration, firstInIteration);
        });

        log.infof("Created all '%d' sessions", count);
    }

    private void createAndCacheSession(Cache<String, SessionEntityWrapper> cache, String realmName) {
        UserSessionEntity userSession = new UserSessionEntity(KeycloakModelUtils.generateId());
        userSession.setRealmId(realmName);
        userSession.setLastSessionRefresh(Time.currentTime());
        cache.put(userSession.getId(), new SessionEntityWrapper(userSession));
    }
}
