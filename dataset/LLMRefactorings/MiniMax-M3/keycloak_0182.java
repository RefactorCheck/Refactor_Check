public class keycloak_0182 {

        protected Response processResult(AuthenticationProcessor.Result result) {
            AuthenticationExecutionModel execution = result.getExecution();
            FlowStatus status = result.getStatus();
    
            logger.debugv("client authenticator {0}: {1}", status, execution.getAuthenticator());
    
            if (status == FlowStatus.SUCCESS) {
                return null;
            }
    
            if (status == FlowStatus.FAILED) {
                if (result.getChallenge() != null) {
                    return sendChallenge(result, execution);
                } else {
                    throw new AuthenticationFlowException(result.getError());
                }
            } else if (status == FlowStatus.FORCE_CHALLENGE) {
                return sendChallenge(result, execution);
            } else if (status == FlowStatus.CHALLENGE) {
                storeAlternativeChallenge(result);
                return sendChallenge(result, execution);
            } else if (status == FlowStatus.FAILURE_CHALLENGE) {
                return sendChallenge(result, execution);
            } else if (status == FlowStatus.ATTEMPTED) {
                logger.warnv("Client authentication was attempted did not complete for {0}", execution.getAuthenticator());
                throw new AuthenticationFlowException(AuthenticationFlowError.GENERIC_AUTHENTICATION_ERROR);
            } else {
                ServicesLogger.LOGGER.unknownResultStatus();
                throw new AuthenticationFlowException(AuthenticationFlowError.INTERNAL_ERROR);
            }
        }

        private void storeAlternativeChallenge(AuthenticationProcessor.Result result) {
            if (alternativeChallenge == null) {
                alternativeChallenge = result.getChallenge();
            }
        }
}
