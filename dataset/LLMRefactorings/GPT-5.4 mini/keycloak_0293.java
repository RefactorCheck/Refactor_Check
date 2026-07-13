public class keycloak_0293 {

        private<R> R sessionAware(String protocol, UserModel user, String scopeParam, String audienceParam, ProtocolResponseGenerator<R> function) {
            AuthenticationSessionModel authSession = null;
            UserSessionModel userSession = null;
            AuthenticationSessionManager authSessionManager = new AuthenticationSessionManager(session);

            try {
                RootAuthenticationSessionModel rootAuthSession = authSessionManager.createAuthenticationSession(realm, false);
                authSession = rootAuthSession.createAuthenticationSession(client);

                authSession.setAuthenticatedUser(user);
                authSession.setProtocol(protocol);
                authSession.setClientNote(OIDCLoginProtocol.ISSUER, Urls.realmIssuer(uriInfo.getBaseUri(), realm.getName()));
                authSession.setClientNote(OIDCLoginProtocol.SCOPE_PARAM, scopeParam);

                userSession = new UserSessionManager(session).createUserSession(authSession.getParentSession().getId(), realm, user, user.getUsername(),
                        clientConnection.getRemoteHost(), "example-auth", false, null, null, UserSessionModel.SessionPersistenceState.PERSISTENT);

                AuthenticationManager.setClientScopesInSession(session, authSession);
                ClientSessionContext clientSessionCtx = TokenManager.attachAuthenticationSession(session, userSession, authSession);

                ClientModel[] audienceClients = getClients(audienceParam);
                if (audienceClients.length > 0) {
                    clientSessionCtx.setAttribute(Constants.REQUESTED_AUDIENCE_CLIENTS, audienceClients);
                }

                return function.generateProtocolResponse(userSession, clientSessionCtx, audienceClients, authSession);

            } finally {
                cleanupSession(authSessionManager, authSession, userSession);
            }
        }

        private void cleanupSession(AuthenticationSessionManager authSessionManager, AuthenticationSessionModel authSession, UserSessionModel userSession) {
            if (authSession != null) {
                authSessionManager.removeAuthenticationSession(realm, authSession, false);
            }
            if (userSession != null) {
                session.sessions().removeUserSession(realm, userSession);
            }
        }
}
