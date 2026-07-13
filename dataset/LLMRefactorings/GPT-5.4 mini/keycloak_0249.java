public class keycloak_0249 {

        public boolean verifyRequiredAction(String executedAction) {
            if (failed()) {
                return false;
            }

            if (!clientCode.isValidAction(AuthenticationSessionModel.Action.REQUIRED_ACTIONS.name())) {
                logger.debugf("Expected required action, but session action is '%s' . Showing expired page now.", authSession.getAction());
                event.error(Errors.INVALID_CODE);

                response = showPageExpired(authSession);

                return false;
            }

            if (!isActionActive(ClientSessionCode.ActionType.USER)) {
                return false;
            }

            if (actionRequest && !isMatchingRequiredAction(executedAction)) {
                return false;
            }
            return true;
        }

        private boolean isMatchingRequiredAction(String executedAction) {
            String currentRequiredAction = authSession.getAuthNote(AuthenticationProcessor.CURRENT_AUTHENTICATION_EXECUTION);
            if (executedAction == null || !executedAction.equals(currentRequiredAction)) {
                logger.debug("required action doesn't match current required action");
                response = redirectToRequiredActions(currentRequiredAction);
                return false;
            }
            return true;
        }
}
