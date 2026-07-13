public class keycloak_0201 {

        @Override
        public void executeOnEvent(ClientPolicyContext context) throws ClientPolicyException {
            switch (context.getEvent()) {
                case REGISTER:
                case UPDATE:
                    if (context instanceof ClientCRUDContext) {
                        verifyRedirectUris((ClientCRUDContext) context);
                    } else {
                        throw invalidRedirectUri(ERR_GENERAL);
                    }
                    return;
                case PRE_AUTHORIZATION_REQUEST:{
                    String redirectUriParam = ((PreAuthorizationRequestContext)context).getRequestParameters()
                            .getFirst(OAuth2Constants.REDIRECT_URI);
                    String clientId = ((PreAuthorizationRequestContext)context).getClientId();
                    ClientModel client = session.getContext().getRealm().getClientByClientId(clientId);
                    if (client == null) {
                        throw invalidRedirectUri("Invalid parameter: clientId");
                    }
                    if (isAuthFlowWithRedirectEnabled(client)) {
                        verifyRedirectUri(redirectUriParam, true);
                    }
                    return;
                }
                case AUTHORIZATION_REQUEST:{
                    ClientModel client = ((AuthorizationRequestContext)context).getClient();
                    String redirectUriParam = ((AuthorizationRequestContext)context).getRedirectUri();
                    if (client == null) {
                        throw invalidRedirectUri("Invalid parameter: clientId");
                    }
                    if (isAuthFlowWithRedirectEnabled(client)) {
                        verifyRedirectUri(redirectUriParam, true);
                    }
                    return;
                }
                default:
            }
        }
}
