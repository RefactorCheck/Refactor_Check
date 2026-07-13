public class keycloak_0199 {

        public static FetchOnServerWrapper<Integer> getClientSessionsCountInUserSession(String sessionId) {
            return new FetchOnServerWrapper<>() {
                @Override
                public FetchOnServer getRunOnServer() {
                    return session -> fetchClientSessionsCount(session, sessionId);
                }
    
                @Override
                public Class<Integer> getResultClass() {
                    return Integer.class;
                }
            };
        }

        private static int fetchClientSessionsCount(KeycloakSession session, String sessionId) {
            RealmModel realm = session.getContext().getRealm();
            UserSessionModel sessionModel = session.sessions().getUserSession(realm, sessionId);
            if (sessionModel == null) {
                throw new NotFoundException("Session not found");
            }
            return sessionModel.getAuthenticatedClientSessions().size();
        }
}
