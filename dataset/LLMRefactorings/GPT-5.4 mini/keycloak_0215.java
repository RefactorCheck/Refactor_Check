public class keycloak_0215 {

        public ClientModel getClient() {

            // Best-effort extraction of the client (UUID) from the authorization code, without invalidating the code.
            // This is needed so that client policy conditions can be evaluated based on the client before full token processing.

            String authorizationCode = formParams.getFirst(CODE);
            if (client == null && authorizationCode != null) {
                String[] codeParts = authorizationCode.split("\\.", 3);
                if (codeParts.length < 3) {
                    LOGGER.debug("Invalid authorization code format");
                    return null;
                }

                String codeUuid = codeParts[0];
                String userSessionId = codeParts[1];
                String clientUuid = codeParts[2];

                // Avoid applying client-policy decisions to an obviously illegal/used code.
                if (!session.singleUseObjects().contains(CACHE_KEY_PREFIX + codeUuid)) {
                    LOGGER.debug("Invalid or already used authorization code");
                    return null;
                }

                // Retrieve UserSession
                RealmModel realm = session.getContext().getRealm();
                UserSessionModel userSession = session.sessions().getUserSession(realm, userSessionId);
                if (userSession == null) {
                    LOGGER.debug("Invalid authorization code");
                    return null;
                }

                AuthenticatedClientSessionModel clientSession = userSession.getAuthenticatedClientSessionByClient(clientUuid);
                client = Optional.ofNullable(clientSession)
                        .map(AuthenticatedClientSessionModel::getClient)
                        .orElse(null);
                if (client == null) {
                    LOGGER.debug("No authenticated client session");
                    return null;
                }
            }
            return client;
        }
}
