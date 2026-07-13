public class keycloak_0083 {

        protected Response handleBrowserAuthenticationRequest(AuthenticationSessionModel authSession, LoginProtocol protocol, boolean isPassive, boolean redirectToAuthentication) {
            AuthenticationFlowModel flow = getAuthenticationFlow(authSession);
            String flowId = flow.getId();
            AuthenticationProcessor processor = createProcessor(authSession, flowId, LoginActionsService.AUTHENTICATE_PATH);
            event.detail(Details.CODE_ID, authSession.getParentSession().getId());
            if (isPassive) {
                // OIDC prompt == NONE or SAML 2 IsPassive flag
                // This means that client is just checking if the user is already completely logged in.
                // We cancel login if any authentication action or required action is required
                try {
                    Response challenge = processor.authenticateOnly();
                    if (challenge != null) {
                        // KEYCLOAK-8043: forward the request with prompt=none to the default provider.
                        if ("true".equals(authSession.getAuthNote(AuthenticationProcessor.FORWARDED_PASSIVE_LOGIN))) {
                            RestartLoginCookie.setRestartCookie(session, authSession);
                            if (redirectToAuthentication) {
                                return processor.redirectToFlow();
                            }
                            // no need to trigger authenticate, just return the challenge we got from authenticateOnly.
                            return challenge;
                        }
                        else {
                            return protocol.sendError(authSession, Error.PASSIVE_LOGIN_REQUIRED, null);
                        }
                    }
    
                    AuthenticationManager.setClientScopesInSession(session, authSession);
    
                    if (processor.nextRequiredAction() != null) {
                        return protocol.sendError(authSession, Error.PASSIVE_INTERACTION_REQUIRED, null);
                    }
    
                } catch (Exception e) {
                    return processor.handleBrowserException(e);
                }
                return processor.finishAuthentication(protocol);
            } else {
                try {
                    RestartLoginCookie.setRestartCookie(session, authSession);
                    if (redirectToAuthentication) {
                        return processor.redirectToFlow();
                    }
                    return processor.authenticate();
                } catch (Exception e) {
                    return processor.handleBrowserException(e);
                }
            }
        }
}
