public class keycloak_0023 {

        @Override
        public void executeOnEvent(ClientPolicyContext context) throws ClientPolicyException {
            switch (context.getEvent()) {
                case REGISTER:
                    if (context instanceof AdminClientRegisterContext || context instanceof DynamicClientRegisterContext) {
                        ClientRepresentation clientRep = ((ClientCRUDContext)context).getProposedClientRepresentation();
                        confirmSecureUris(clientRep);
    
                        // Use rootUrl as default redirectUrl to avoid creation of redirectUris with wildcards, which is done at later stages during client creation
                        if (clientRep.getRootUrl() != null && (clientRep.getRedirectUris() == null || clientRep.getRedirectUris().isEmpty())) {
                            logger.debugf("Setup Redirect URI = %s for client %s", clientRep.getRootUrl(), clientRep.getClientId());
                            clientRep.setRedirectUris(Collections.singletonList(clientRep.getRootUrl()));
                        }
                    } else {
                        throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "not allowed input format.");
                    }
                    return;
                case UPDATE:
                    if (context instanceof AdminClientUpdateContext || context instanceof DynamicClientUpdateContext) {
                        confirmSecureUris(((ClientCRUDContext)context).getProposedClientRepresentation());
                    } else {
                        throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "not allowed input format.");
                    }
                    return;
                case AUTHORIZATION_REQUEST:
                    confirmSecureRedirectUri(((AuthorizationRequestContext)context).getRedirectUri());
                    return;
                default:
            }
        }
}
