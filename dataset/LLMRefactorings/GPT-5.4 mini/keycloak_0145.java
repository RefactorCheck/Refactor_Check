public class keycloak_0145 {

        public static AuthenticationFlowModel resolveDirectGrantFlow(AuthenticationSessionModel authSession) {
            AuthenticationFlowModel directGrantFlow = null;
            ClientModel client = authSession.getClient();
    
            // check if specific flow has been requested
            String requestedFlowAlias = authSession.getAuthNote(Constants.REQUESTED_AUTHENTICATION_FLOW);
            if (requestedFlowAlias != null){
                directGrantFlow = authSession.getRealm().getFlowByAlias(requestedFlowAlias);
                // validate flow exists
                if (directGrantFlow == null){
                    throw new ModelException("Client " + client.getClientId() + " has requested browser flow " + requestedFlowAlias + ", but this flow does not exist.");
                } else {
                    return directGrantFlow;
                }
            }
    
            directGrantFlow = resolveBindingOverrideFlowForClient(client, AuthenticationFlowBindings.DIRECT_GRANT_BINDING);
            if (directGrantFlow != null) {
                return directGrantFlow;
            }
            return authSession.getRealm().getDirectGrantFlow();
        }
}
