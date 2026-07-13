public class keycloak_0024 {

    private boolean matchConditionInFlow(AuthenticationFlowContext context, String flowId) {
        List<AuthenticationExecutionModel> requiredExecutions = new LinkedList<>();
        List<AuthenticationExecutionModel> alternativeExecutions = new LinkedList<>();
        context.getRealm().getAuthenticationExecutionsStream(flowId)
                .filter(e -> !isConditionalExecution(context, e))
                .filter(e -> isApplicableExecution(context, e))
                .forEachOrdered(e -> {
                    if (e.isRequired()) {
                        requiredExecutions.add(e);
                    } else if (e.isAlternative()) {
                        alternativeExecutions.add(e);
                    }
                });
        if (!requiredExecutions.isEmpty()) {
            return requiredExecutions.stream().allMatch(e -> isConfiguredFor(e, context));
        } else  if (!alternativeExecutions.isEmpty()) {
            return alternativeExecutions.stream().anyMatch(e -> isConfiguredFor(e, context));
        }
        return true;
    }

    private boolean isApplicableExecution(AuthenticationFlowContext context, AuthenticationExecutionModel e) {
        return !Objects.equals(context.getExecution().getId(), e.getId()) && !e.isAuthenticatorFlow();
    }
}
