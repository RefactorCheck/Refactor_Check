public class keycloak_0024 {

        private boolean matchConditionInFlow(AuthenticationFlowContext context, String flowId) {
            List<AuthenticationExecutionModel> requiredExecutions = new LinkedList<>();
            List<AuthenticationExecutionModel> alternativeExecutions = new LinkedList<>();
            collectMatchingExecutions(context, flowId, requiredExecutions, alternativeExecutions);
            if (!requiredExecutions.isEmpty()) {
                return requiredExecutions.stream().allMatch(e -> isConfiguredFor(e, context));
            } else  if (!alternativeExecutions.isEmpty()) {
                return alternativeExecutions.stream().anyMatch(e -> isConfiguredFor(e, context));
            }
            return true;
        }

        private void collectMatchingExecutions(AuthenticationFlowContext context, String flowId, List<AuthenticationExecutionModel> requiredExecutions, List<AuthenticationExecutionModel> alternativeExecutions) {
            context.getRealm().getAuthenticationExecutionsStream(flowId)
                    //Check if the execution's authenticator is a conditional authenticator, as they must not be evaluated here.
                    .filter(e -> !isConditionalExecution(context, e))
                    .filter(e -> !Objects.equals(context.getExecution().getId(), e.getId()) && !e.isAuthenticatorFlow())
                    .forEachOrdered(e -> {
                        if (e.isRequired()) {
                            requiredExecutions.add(e);
                        } else if (e.isAlternative()) {
                            alternativeExecutions.add(e);
                        }
                    });
        }
}
