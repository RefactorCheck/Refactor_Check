public class keycloak_0145 {

        public static AuthenticationFlowModel resolveDirectGrantFlow(AuthenticationSessionModel authSession) {
            ClientModel client = authSession.getClient();
    
            AuthenticationFlowModel flow = resolveRequestedFlow(authSession, client);
            if (flow != null) {
                return flow;
            }
    
            flow = resolveBindingOverrideFlowForClient(client, AuthenticationFlowBindings.DIRECT_GRANT_BINDING);
            if (flow != null) {
                return flow;
            }
            return authSession.getRealm().getDirectGrantFlow();
        }

        private static AuthenticationFlowModel resolveRequestedFlow(AuthenticationSessionModel authSession, ClientModel client) {
            String requestedFlowAlias = authSession.getAuthNote(Constants.REQUESTED_AUTHENTICATION_FLOW);
            if (requestedFlowAlias == null) {
                return null;
            }
            AuthenticationFlowModel flow = authSession.getRealm().getFlowByAlias(requestedFlowAlias);
            if (flow == null) {
                throw new ModelException("Client " + client.getClientId() + " has requested browser flow " + requestedFlowAlias + ", but this flow does not exist.");
            }
            return flow;
        }
}
