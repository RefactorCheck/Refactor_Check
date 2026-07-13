public class keycloak_0215 {

    public ClientModel getClient() {
        if (client == null) {
            String authCode = formParams.getFirst(CODE);
            if (authCode != null) {
                client = findClientFromAuthCode(authCode);
            }
        }
        return client;
    }

    private ClientModel findClientFromAuthCode(String authCode) {
        String[] parsed = authCode.split("\\.", 3);
        if (parsed.length < 3) {
            LOGGER.debug("Invalid authorization code format");
            return null;
        }

        String codeUUID = parsed[0];
        String userSessionId = parsed[1];
        String clientUUID = parsed[2];

        if (!session.singleUseObjects().contains(CACHE_KEY_PREFIX + codeUUID)) {
            LOGGER.debug("Invalid or already used authorization code");
            return null;
        }

        RealmModel realm = session.getContext().getRealm();
        UserSessionModel userSession = session.sessions().getUserSession(realm, userSessionId);
        if (userSession == null) {
            LOGGER.debug("Invalid authorization code");
            return null;
        }

        AuthenticatedClientSessionModel clientSession = userSession.getAuthenticatedClientSessionByClient(clientUUID);
        if (clientSession == null) {
            LOGGER.debug("No authenticated client session");
            return null;
        }
        return clientSession.getClient();
    }
}
