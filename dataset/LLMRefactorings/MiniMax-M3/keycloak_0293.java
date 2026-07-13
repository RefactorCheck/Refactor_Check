public class keycloak_0293 {

        private static final String AUTH_METHOD = "example-auth";

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
                        clientConnection.getRemoteHost(), AUTH_METHOD, false, null, null, UserSessionModel.SessionPersistenceState.PERSISTENT);
    
                AuthenticationManager.setClientScopesInSession(session, authSession);
                ClientSessionContext clientSessionCtx = TokenManager.attachAuthenticationSession(session, userSession, authSession);
    
                ClientModel[] audienceClients = getClients(audienceParam);
                if (audienceClients.length > 0) {
                    clientSessionCtx.setAttribute(Constants.REQUESTED_AUDIENCE_CLIENTS, audienceClients);
                }
    
                return function.generateProtocolResponse(userSession, clientSessionCtx, audienceClients, authSession);
    
            } finally {
                if (authSession != null) {
                    authSessionManager.removeAuthenticationSession(realm, authSession, false);
                }
                if (userSession != null) {
                    session.sessions().removeUserSession(realm, userSession);
                }
            }
        }
}
