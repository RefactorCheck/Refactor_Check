public class keycloak_0199 {

        public static FetchOnServerWrapper<Integer> getClientSessionsCountInUserSession(String sessionId) {
            return new FetchOnServerWrapper<>() {
                @Override
                public FetchOnServer getRunOnServer() {
                    return session -> {
                        RealmModel realm = session.getContext().getRealm();

                        UserSessionModel userSession = session.sessions().getUserSession(realm, sessionId);
                        if (userSession == null) {
                            throw new NotFoundException("Session not found");
                        }

                        return userSession.getAuthenticatedClientSessions().size();
                    };
                }

                @Override
                public Class<Integer> getResultClass() {
                    return Integer.class;
                }
            };
        }
}
