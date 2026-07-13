public class keycloak_0071 {

        private static void populateClientSessions(Map<String, ImmutableUserSessionModel> userSessionMap, SessionExpirationPredicates expirationPredicates, RemoteCache<ClientSessionKey, RemoteAuthenticatedClientSessionEntity> cache, int batchSize) {
            var query = ClientSessionQueries.fetchClientSessions(cache, userSessionMap.keySet());
            QueryHelper.streamAll(query, batchSize, Function.identity()).forEach(entity -> {
                var userSession = userSessionMap.get(entity.getUserSessionId());
                var client = expirationPredicates.realm().getClientById(entity.getClientId());
                if (client == null || userSession == null) {
                    return;
                }
                if (expirationPredicates.isClientSessionExpired(entity, userSession.started(), userSession.rememberMe(), client)) {
                    return;
                }
                var copy = new ImmutableClientSession(
                        entity.createId(),
                        client,
                        userSession,
                        Map.copyOf(entity.getNotes()),
                        entity.getRedirectUri(),
                        entity.getAction(),
                        entity.getProtocol(),
                        entity.getTimestamp(),
                        entity.getStarted()
                );
                userSession.clientSessions().put(entity.getClientId(), copy);
            });
        }
}
