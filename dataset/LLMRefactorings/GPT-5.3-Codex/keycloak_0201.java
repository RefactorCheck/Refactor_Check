@Override
        public void executeOnEvent(ClientPolicyContext clientPolicyContext) throws ClientPolicyException {
            switch (clientPolicyContext.getEvent()) {
                case REGISTER:
                case UPDATE:
                    if (clientPolicyContext instanceof ClientCRUDContext) {
                        verifyRedirectUris((ClientCRUDContext) clientPolicyContext);
                    } else {
                        throw invalidRedirectUri(ERR_GENERAL);
                    }
                    return;
                case PRE_AUTHORIZATION_REQUEST:{
                    String redirectUriParam = ((PreAuthorizationRequestContext)clientPolicyContext).getRequestParameters()
                            .getFirst(OAuth2Constants.REDIRECT_URI);
                    String clientId = ((PreAuthorizationRequestContext)clientPolicyContext).getClientId();
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
                    ClientModel client = ((AuthorizationRequestContext)clientPolicyContext).getClient();
                    String redirectUriParam = ((AuthorizationRequestContext)clientPolicyContext).getRedirectUri();
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
