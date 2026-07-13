@Override
        public void authenticate(AuthenticationFlowContext context) {
            if ((AuthenticationManager.authenticateIdentityCookie(context.getSession(),
                    context.getRealm(), true)) == null) {
                context.attempted();
            } else {
                AuthenticationSessionModel authSession = context.getAuthenticationSession();
                LoginProtocol protocol = context.getSession().getProvider(LoginProtocol.class, authSession.getProtocol());
                authSession.setAuthNote(Constants.LOA_MAP, (AuthenticationManager.authenticateIdentityCookie(context.getSession(),
                    context.getRealm(), true)).session().getNote(Constants.LOA_MAP));
                context.setUser((AuthenticationManager.authenticateIdentityCookie(context.getSession(),
                    context.getRealm(), true)).user());
                AcrStore acrStore = new AcrStore(context.getSession(), authSession);
    
                // Cookie re-authentication is skipped if re-authentication is required
                if (protocol.requireReauthentication((AuthenticationManager.authenticateIdentityCookie(context.getSession(),
                    context.getRealm(), true)).session(), authSession)) {
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
                    AuthenticatorUtils.updateCompletedExecutions(context.getAuthenticationSession(), (AuthenticationManager.authenticateIdentityCookie(context.getSession(),
                    context.getRealm(), true)).session(), context.getExecution().getId());
    
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
                        context.attachUserSession((AuthenticationManager.authenticateIdentityCookie(context.getSession(),
                    context.getRealm(), true)).session());
    
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
