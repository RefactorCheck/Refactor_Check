public class keycloak_0025 {

    @Override
    public void executeOnEvent(ClientPolicyContext context) throws ClientPolicyException {
        switch (context.getEvent()) {
            case REGISTER:
                processClientCRUDContext(context, AdminClientRegisterContext.class, DynamicClientRegisterContext.class);
                return;
            case UPDATE:
                processClientCRUDContext(context, AdminClientUpdateContext.class, DynamicClientUpdateContext.class);
                return;
            default:
        }
    }

    private void processClientCRUDContext(ClientPolicyContext context, Class<?> type1, Class<?> type2) throws ClientPolicyException {
        if (type1.isInstance(context) || type2.isInstance(context)) {
            validateClientUris(((ClientCRUDContext) context).getProposedClientRepresentation());
        } else {
            throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "not allowed input format.");
        }
    }
}
