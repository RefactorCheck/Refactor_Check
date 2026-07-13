public static AuthenticationFlowModel resolveDirectGrantFlow(AuthenticationSessionModel authSession) {
            AuthenticationFlowModel flow = null;
            ClientModel client = authSession.getClient();
    
            // check if specific flow has been requested
            String requestedFlowAlias = authSession.getAuthNote(Constants.REQUESTED_AUTHENTICATION_FLOW);
            if (requestedFlowAlias != null){
                flow = authSession.getRealm().getFlowByAlias(requestedFlowAlias);
                // validate flow exists
                if (flow == null){
                    throw new ModelException("Client " + client.getClientId() + " has requested browser flow " + requestedFlowAlias + ", but this flow does not exist.");
                } else {
                    return flow;
                }
            }
    
            flow = resolveBindingOverrideFlowForClient(client, AuthenticationFlowBindings.DIRECT_GRANT_BINDING);
            if (flow != null) {
                return flow;
            }
            return authSession.getRealm().getDirectGrantFlow();
        }
