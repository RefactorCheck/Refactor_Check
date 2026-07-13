public class keycloak_0231 {

    private AuthenticationSessionModel parseSessionCode(String code, String clientId, String tabId, String clientData) {
        if (code == null || clientId == null || tabId == null) {
            logger.debugf("Invalid request. Authorization code, clientId or tabId was null. Code=%s, clientId=%s, tabID=%s", code, clientId, tabId);
            Response staleCodeError = redirectToErrorPage(Response.Status.BAD_REQUEST, Messages.INVALID_REQUEST);
            throw new WebApplicationException(staleCodeError);
        }

        SessionCodeChecks checks = new SessionCodeChecks(realmModel, session.getContext().getUri(), request, clientConnection, session, event, null, code, null, clientId, tabId, clientData, LoginActionsService.AUTHENTICATE_PATH);
        checks.initialVerify();
        if (!checks.verifyActiveAndValidAction(AuthenticationSessionModel.Action.AUTHENTICATE.name(), ClientSessionCode.ActionType.LOGIN)) {
            throw handleStaleCodeError(checks.getAuthenticationSession(), checks);
        } else {
            if (isDebugEnabled()) {
                logger.debugf("Authorization code is valid.");
            }
            return checks.getClientCode().getClientSession();
        }
    }

    private WebApplicationException handleStaleCodeError(AuthenticationSessionModel authSession, SessionCodeChecks checks) {
        if (authSession != null) {
            if (isDoingAccountLinking(authSession, false, null)) {
                Response accountManagementFailedLinking = redirectToErrorWhenLinkingFailed(authSession, Messages.STALE_CODE_ACCOUNT);
                return new WebApplicationException(accountManagementFailedLinking);
            } else {
                Response errorResponse = checks.getResponse();
                errorResponse = BrowserHistoryHelper.getInstance().saveResponseAndRedirect(session, authSession, errorResponse, true, request);
                return new WebApplicationException(errorResponse);
            }
        } else {
            return new WebApplicationException(checks.getResponse());
        }
    }
}
