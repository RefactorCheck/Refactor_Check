public class keycloak_0123 {

        @Override
        protected void authenticateImpl(AuthenticationFlowContext context, SerializedBrokeredIdentityContext serializedCtx, BrokeredIdentityContext brokerContext) {
            AuthenticationSessionModel authSession = context.getAuthenticationSession();

            String existingUserInfo = authSession.getAuthNote(EXISTING_USER_INFO);
            if (existingUserInfo == null) {
                ServicesLogger.LOGGER.noDuplicationDetected();
                context.attempted();
                return;
            }

            ExistingUserInfo duplicationInfo = ExistingUserInfo.deserialize(existingUserInfo);
            Response challenge = context.form()
                    .setStatus(Response.Status.OK)
                    .setAttribute(LoginFormsProvider.IDENTITY_PROVIDER_BROKER_CONTEXT, brokerContext)
                    .setAttribute("hideReviewButton", shouldHideReviewButton(authSession, context) ? Boolean.TRUE : null)
                    .setError(Messages.FEDERATED_IDENTITY_CONFIRM_LINK_MESSAGE, duplicationInfo.getDuplicateAttributeName(), duplicationInfo.getDuplicateAttributeValue())
                    .createIdpLinkConfirmLinkPage();
            context.challenge(challenge);
        }

        private boolean shouldHideReviewButton(AuthenticationSessionModel authSession, AuthenticationFlowContext context) {
            return authSession.getExecutionStatus().entrySet().stream()
                    .filter(entry -> CommonClientSessionModel.ExecutionStatus.SUCCESS.equals(entry.getValue()))
                    .map(entry -> context.getRealm().getAuthenticationExecutionById(entry.getKey()))
                    .filter(exec -> IdpReviewProfileAuthenticatorFactory.PROVIDER_ID.equals(exec.getAuthenticator()))
                    .findAny()
                    .isEmpty();
        }
}
