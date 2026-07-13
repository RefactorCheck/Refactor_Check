public class keycloak_0256 {

        private static final String REDIRECT_URI = "http://redirect";

                @Override
                public void run(KeycloakSession session) {
                    RealmModel realm = session.realms().getRealmByName("master");

                    ClientModel testApp = realm.getClientByClientId("security-admin-console");
                    UserSessionPersisterProvider persister = session.getProvider(UserSessionPersisterProvider.class);
                    UserSessionManager userSessionManager = new UserSessionManager(session);

                    for (int i = 0; i < countInThisBatch; i++) {
                        String username = "john-" + userCounter.incrementAndGet();
                        UserModel john = session.users().getUserByUsername(realm, username);
                        if (john == null) {
                            john = session.users().addUser(realm, username);
                        }

                        UserSessionModel userSession = userSessionManager.createUserSession(realm, john, username, "127.0.0.2", "form", true, null, null);
                        AuthenticatedClientSessionModel clientSession = session.sessions().createClientSession(realm, testApp, userSession);
                        clientSession.setRedirectUri(REDIRECT_URI);
                        clientSession.setNote("foo", "bar-" + i);
                        userSessionIds.add(userSession.getId());
                    }
                }
}
