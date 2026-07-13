public class keycloak_0294 {

        @Override
        public void authenticate(AuthenticationFlowContext context) {
            AuthenticationManager.AuthResult authResult = AuthenticationManager.authenticateIdentityCookie(context.getSession(),
                    context.getRealm(), true);
            if (authResult == null) {
                context.attempted();
            } else {
                AuthenticationSessionModel authSession = context.getAuthenticationSession();
                LoginProtocol protocol = context.getSession().getProvider(LoginProtocol.class, authSession.getProtocol());
                authSession.setAuthNote(Constants.LOA_MAP, authResult.session().getNote(Constants.LOA_MAP));
                context.setUser(authResult.user());
                AcrStore acrStore = new AcrStore(context.getSession(), authSession);
    
                // Cookie re-authentication is skipped if re-authentication is required
                if (protocol.requireReauthentication(authResult.session(), authSession)) {
                    // Full re-authentication, so we start with no loa
                    acrStore.setLevelAuthenticatedToCurrentRequest(Constants.NO_LOA);
                    authSession.setAuthNote(AuthenticationManager.FORCED_REAUTHENTICATION, "true");
                    context.setForwardedInfoMessage(Messages.REAUTHENTICATE);
                    context.attempted();
                } else if(AuthenticatorUtil.isForkedFlow(authSession)){
                    context.attempted();
                } else {
                    String topLevelFlowId = context.getTopLevelFlow().getId();
                    int previouslyAuthenticatedLevel = acrStore.getHighestAuthenticatedLevelFromPreviousAuthentication(topLevelFlowId);
                    AuthenticatorUtils.updateCompletedExecutions(context.getAuthenticationSession(), authResult.session(), context.getExecution().getId());
    
                    if (acrStore.getRequestedLevelOfAuthentication(context.getTopLevelFlow()) > previouslyAuthenticatedLevel) {
                        // Step-up authentication, we keep the loa from the existing user session.
                        // The cookie alone is not enough and other authentications must follow.
                        acrStore.setLevelAuthenticatedToCurrentRequest(previouslyAuthenticatedLevel);
    
                        if (authSession.getClientNote(Constants.KC_ACTION) != null) {
                            context.setForwardedInfoMessage(Messages.AUTHENTICATE_STRONG);
                        }
    
                        context.attempted();
                    } else {
                        // Cookie only authentication
                        acrStore.setLevelAuthenticatedToCurrentRequest(previouslyAuthenticatedLevel);
                        authSession.setAuthNote(AuthenticationManager.SSO_AUTH, "true");
                        context.attachUserSession(authResult.session());
    
                        if (isOrganizationContext(context)) {
                            // if re-authenticating in the scope of an organization, an organization must be resolved prior to authenticating the user
                            context.attempted();
                        } else {
                            context.success();
                        }
                    }
                }
            }
    
        }
}
