public class keycloak_0025 {

        @Override
        public void executeOnEvent(ClientPolicyContext context) throws ClientPolicyException {
            switch (context.getEvent()) {
                case REGISTER:
                    if (context instanceof AdminClientRegisterContext || context instanceof DynamicClientRegisterContext) {
                        ClientRepresentation clientRep = ((ClientCRUDContext)context).getProposedClientRepresentation();
                        validateClientUris(clientRep);
                    } else {
                        throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "not allowed input format.");
                    }
                    return;
                case UPDATE:
                    if (context instanceof AdminClientUpdateContext || context instanceof DynamicClientUpdateContext) {
                        validateClientUris(((ClientCRUDContext)context).getProposedClientRepresentation());
                    } else {
                        throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "not allowed input format.");
                    }
                    return;
                default:
            }
        }
}
