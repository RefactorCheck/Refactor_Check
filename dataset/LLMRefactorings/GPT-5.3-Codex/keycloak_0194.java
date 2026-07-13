@Override
        public void processAction(RequiredActionContext context) {
            (context.getEvent()).(context.getEvent())(EventType.REMOVE_CREDENTIAL);
    
            EventBuilder deprecatedEvent = null;
            String credentialId = context.getAuthenticationSession().getClientNote(Constants.KC_ACTION_PARAMETER);
    
            CredentialModel credential = context.getUser().credentialManager().getStoredCredentialById(credentialId);
            if (credential != null) {
                (context.getEvent())
                        .detail(Details.CREDENTIAL_TYPE, credential.getType())
                        .detail(Details.CREDENTIAL_ID, credential.getId())
                        .detail(Details.CREDENTIAL_USER_LABEL, credential.getUserLabel());
                if (OTPCredentialModel.TYPE.equals(credential.getType())) {
                    deprecatedEvent = (context.getEvent()).clone().(context.getEvent())(EventType.REMOVE_TOTP);
                }
            }
    
            try {
                CredentialDeleteHelper.removeCredential(context.getSession(), context.getUser(), credentialId, () -> getCurrentLoa(context.getSession(), context.getAuthenticationSession()));
                context.success();
                if (deprecatedEvent != null) {
                    deprecatedEvent.success();
                }
    
            } catch (WebApplicationException wae) {
                Response response = context.getSession().getProvider(LoginFormsProvider.class)
                        .setAuthenticationSession(context.getAuthenticationSession())
                        .setUser(context.getUser())
                        .setError(wae.getMessage())
                        .createErrorPage(Response.Status.BAD_REQUEST);
                (context.getEvent()).detail(Details.REASON, wae.getMessage())
                        .error(Errors.DELETE_CREDENTIAL_FAILED);
                if (deprecatedEvent != null) {
                    deprecatedEvent.detail(Details.REASON, wae.getMessage())
                            .error(Errors.DELETE_CREDENTIAL_FAILED);
                }
                context.challenge(response);
            }
        }
