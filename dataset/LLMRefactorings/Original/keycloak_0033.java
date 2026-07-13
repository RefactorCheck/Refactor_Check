public class keycloak_0033 {

        public Response handleBrowserExceptionList(AuthenticationFlowException e) {
            LoginFormsProvider forms = session.getProvider(LoginFormsProvider.class).setAuthenticationSession(authenticationSession);
            ServicesLogger.LOGGER.failedAuthentication(e);
            forms.addError(new FormMessage(Messages.UNEXPECTED_ERROR_HANDLING_REQUEST));
            for (AuthenticationFlowException afe : e.getAfeList()) {
                ServicesLogger.LOGGER.failedAuthentication(afe);
                switch (afe.getError()){
                    case INVALID_USER:
                        event.error(Errors.USER_NOT_FOUND);
                        forms.addError(new FormMessage(Messages.INVALID_USER));
                        break;
                    case USER_DISABLED:
                        event.error(Errors.USER_DISABLED);
                        forms.addError(new FormMessage(Messages.ACCOUNT_DISABLED));
                        break;
                    case USER_TEMPORARILY_DISABLED:
                        event.error(Errors.USER_TEMPORARILY_DISABLED);
                        forms.addError(new FormMessage(Messages.INVALID_USER));
                        break;
                    case INVALID_CLIENT_SESSION:
                        event.error(Errors.INVALID_CODE);
                        forms.addError(new FormMessage(Messages.INVALID_CODE));
                        break;
                    case EXPIRED_CODE:
                        event.error(Errors.EXPIRED_CODE);
                        forms.addError(new FormMessage(Messages.EXPIRED_CODE));
                        break;
                    case DISPLAY_NOT_SUPPORTED:
                        event.error(Errors.DISPLAY_UNSUPPORTED);
                        forms.addError(new FormMessage(Messages.DISPLAY_UNSUPPORTED));
                        break;
                    case CREDENTIAL_SETUP_REQUIRED:
                        event.error(Errors.INVALID_USER_CREDENTIALS);
                        forms.addError(new FormMessage(Messages.CREDENTIAL_SETUP_REQUIRED));
                        break;
                }
            }
            return forms.createErrorPage(Response.Status.BAD_REQUEST);
        }
}
