public class keycloak_0194 {

        @Override
        public void processAction(RequiredActionContext context) {
            EventBuilder event = context.getEvent();
            event.event(EventType.REMOVE_CREDENTIAL);

            EventBuilder deprecatedEvent = null;
            String credentialId = context.getAuthenticationSession().getClientNote(Constants.KC_ACTION_PARAMETER);

            CredentialModel credential = context.getUser().credentialManager().getStoredCredentialById(credentialId);
            if (credential != null) {
                event
                        .detail(Details.CREDENTIAL_TYPE, credential.getType())
                        .detail(Details.CREDENTIAL_ID, credential.getId())
                        .detail(Details.CREDENTIAL_USER_LABEL, credential.getUserLabel());
                if (OTPCredentialModel.TYPE.equals(credential.getType())) {
                    deprecatedEvent = event.clone().event(EventType.REMOVE_TOTP);
                }
            }

            try {
                CredentialDeleteHelper.removeCredential(context.getSession(), context.getUser(), credentialId, () -> getCurrentLoa(context.getSession(), context.getAuthenticationSession()));
                context.success();
                if (deprecatedEvent != null) {
                    deprecatedEvent.success();
                }

            } catch (WebApplicationException wae) {
                String reason = wae.getMessage();
                Response response = context.getSession().getProvider(LoginFormsProvider.class)
                        .setAuthenticationSession(context.getAuthenticationSession())
                        .setUser(context.getUser())
                        .setError(reason)
                        .createErrorPage(Response.Status.BAD_REQUEST);
                event.detail(Details.REASON, reason)
                        .error(Errors.DELETE_CREDENTIAL_FAILED);
                if (deprecatedEvent != null) {
                    deprecatedEvent.detail(Details.REASON, reason)
                            .error(Errors.DELETE_CREDENTIAL_FAILED);
                }
                context.challenge(response);
            }
        }
}
