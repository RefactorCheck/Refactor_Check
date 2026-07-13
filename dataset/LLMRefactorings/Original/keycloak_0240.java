public class keycloak_0240 {

        private static String getFlowIdOfTheHighestUsefulFlow(AuthenticationProcessor processor, AuthenticationExecutionModel execution) {
            String flowId = null;
            RealmModel realm = processor.getRealm();
    
            while (true) {
                if (execution.isAlternative()) {
                    //Consider parent flow as we need to get all alternative executions to be able to list their credentials
                    flowId = execution.getParentFlow();
                } else if (execution.isRequired()  || execution.isConditional()) {
                    if (execution.isAuthenticatorFlow()) {
                        flowId = execution.getFlowId();
                    }
    
                    // Find the corresponding execution. If it is 1st REQUIRED execution in the particular subflow, we need to consider parent flow as well
                    List<AuthenticationExecutionModel> executions = realm.getAuthenticationExecutionsStream(execution.getParentFlow())
                            .collect(Collectors.toList());
                    int executionIndex = executions.indexOf(execution);
                    if (executionIndex != 0) {
                        return flowId;
                    } else {
                        flowId = execution.getParentFlow();
                    }
                }
    
                AuthenticationFlowModel flow = realm.getAuthenticationFlowById(flowId);
                if (flow.isTopLevel()) {
                    return flowId;
                }
                execution = realm.getAuthenticationExecutionByFlowId(flowId);
            }
        }
}
