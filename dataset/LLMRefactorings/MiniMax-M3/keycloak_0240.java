public class keycloak_0240 {

        private static String getFlowIdOfTheHighestUsefulFlow(AuthenticationProcessor processor, AuthenticationExecutionModel execution) {
            String flowId = null;
            RealmModel realm = processor.getRealm();
    
            while (true) {
                if (execution.isAlternative()) {
                    flowId = execution.getParentFlow();
                } else if (execution.isRequired()  || execution.isConditional()) {
                    if (execution.isAuthenticatorFlow()) {
                        flowId = execution.getFlowId();
                    }
    
                    if (!isFirstExecutionInParentFlow(realm, execution)) {
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

        private static boolean isFirstExecutionInParentFlow(RealmModel realm, AuthenticationExecutionModel execution) {
            List<AuthenticationExecutionModel> executions = realm.getAuthenticationExecutionsStream(execution.getParentFlow())
                    .collect(Collectors.toList());
            return executions.indexOf(execution) == 0;
        }
}
