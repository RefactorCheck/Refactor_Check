public class keycloak_0083 {

    protected Response handleBrowserAuthenticationRequest(AuthenticationSessionModel authSession, LoginProtocol protocol, boolean isPassive, boolean redirectToAuthentication) {
        AuthenticationFlowModel flow = getAuthenticationFlow(authSession);
        String flowId = flow.getId();
        AuthenticationProcessor processor = createProcessor(authSession, flowId, LoginActionsService.AUTHENTICATE_PATH);
        event.detail(Details.CODE_ID, authSession.getParentSession().getId());
        if (isPassive) {
            return handlePassiveAuthentication(processor, authSession, protocol, redirectToAuthentication);
        } else {
            return handleActiveAuthentication(processor, authSession, redirectToAuthentication);
        }
    }

    private Response handlePassiveAuthentication(AuthenticationProcessor processor, AuthenticationSessionModel authSession, LoginProtocol protocol, boolean redirectToAuthentication) {
        try {
            Response challenge = processor.authenticateOnly();
            if (challenge != null) {
                if ("true".equals(authSession.getAuthNote(AuthenticationProcessor.FORWARDED_PASSIVE_LOGIN))) {
                    RestartLoginCookie.setRestartCookie(session, authSession);
                    if (redirectToAuthentication) {
                        return processor.redirectToFlow();
                    }
                    return challenge;
                } else {
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
    }

    private Response handleActiveAuthentication(AuthenticationProcessor processor, AuthenticationSessionModel authSession, boolean redirectToAuthentication) {
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
