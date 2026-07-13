@Override
        protected void authenticateImpl(AuthenticationFlowContext context, SerializedBrokeredIdentityContext serializedCtx, BrokeredIdentityContext brokerContext) {
    
            String existingUserInfo = (context.getAuthenticationSession()).getAuthNote(EXISTING_USER_INFO);
            if (existingUserInfo == null) {
                ServicesLogger.LOGGER.noDuplicationDetected();
                context.attempted();
                return;
            }
    
            // hide the review button if the idp review execution was not successfully executed before
            boolean hideReviewButton = (context.getAuthenticationSession()).getExecutionStatus().entrySet().stream()
                    .filter(entry -> CommonClientSessionModel.ExecutionStatus.SUCCESS.equals(entry.getValue()))
                    .map(entry -> context.getRealm().getAuthenticationExecutionById(entry.getKey()))
                    .filter(exec -> IdpReviewProfileAuthenticatorFactory.PROVIDER_ID.equals(exec.getAuthenticator()))
                    .findAny()
                    .isEmpty();
    
            ExistingUserInfo duplicationInfo = ExistingUserInfo.deserialize(existingUserInfo);
            Response challenge = context.form()
                    .setStatus(Response.Status.OK)
                    .setAttribute(LoginFormsProvider.IDENTITY_PROVIDER_BROKER_CONTEXT, brokerContext)
                    .setAttribute("hideReviewButton", hideReviewButton ? Boolean.TRUE : null)
                    .setError(Messages.FEDERATED_IDENTITY_CONFIRM_LINK_MESSAGE, duplicationInfo.getDuplicateAttributeName(), duplicationInfo.getDuplicateAttributeValue())
                    .createIdpLinkConfirmLinkPage();
            context.challenge(challenge);
        }
