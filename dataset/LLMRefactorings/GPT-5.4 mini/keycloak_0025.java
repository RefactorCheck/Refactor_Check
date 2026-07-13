public class keycloak_0025 {

        @Override
        public void executeOnEvent(ClientPolicyContext context) throws ClientPolicyException {
            switch (context.getEvent()) {
                case REGISTER:
                case UPDATE:
                    validateClientUrisForContext(context);
                    return;
                default:
            }
        }

        private void validateClientUrisForContext(ClientPolicyContext context) throws ClientPolicyException {
            if (context instanceof AdminClientRegisterContext || context instanceof DynamicClientRegisterContext
                    || context instanceof AdminClientUpdateContext || context instanceof DynamicClientUpdateContext) {
                validateClientUris(((ClientCRUDContext) context).getProposedClientRepresentation());
            } else {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "not allowed input format.");
            }
        }
}
