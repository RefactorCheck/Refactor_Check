public class keycloak_0182 {

        protected Response processResult(AuthenticationProcessor.Result result) {
            AuthenticationExecutionModel execution = result.getExecution();
            FlowStatus flowStatus = result.getStatus();

            logger.debugv("client authenticator {0}: {1}", flowStatus, execution.getAuthenticator());

            if (flowStatus == FlowStatus.SUCCESS) {
                return null;
            }

            if (flowStatus == FlowStatus.FAILED) {
                if (result.getChallenge() != null) {
                    return sendChallenge(result, execution);
                } else {
                    throw new AuthenticationFlowException(result.getError());
                }
            } else if (flowStatus == FlowStatus.FORCE_CHALLENGE) {
                return sendChallenge(result, execution);
            } else if (flowStatus == FlowStatus.CHALLENGE) {

                if (alternativeChallenge == null) {
                    alternativeChallenge = result.getChallenge();
                }
                return sendChallenge(result, execution);
            } else if (flowStatus == FlowStatus.FAILURE_CHALLENGE) {
                return sendChallenge(result, execution);
            } else if (flowStatus == FlowStatus.ATTEMPTED) {
                logger.warnv("Client authentication was attempted did not complete for {0}", execution.getAuthenticator());
                throw new AuthenticationFlowException(AuthenticationFlowError.GENERIC_AUTHENTICATION_ERROR);
            } else {
                ServicesLogger.LOGGER.unknownResultStatus();
                throw new AuthenticationFlowException(AuthenticationFlowError.INTERNAL_ERROR);
            }
        }
}
