@Override
        public void executeOnEvent(ClientPolicyContext clientPolicyContext) throws ClientPolicyException {
            switch (clientPolicyContext.getEvent()) {
                case REGISTER:
                    if (clientPolicyContext instanceof AdminClientRegisterContext || clientPolicyContext instanceof DynamicClientRegisterContext) {
                        ClientRepresentation clientRep = ((ClientCRUDContext)clientPolicyContext).getProposedClientRepresentation();
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
                    if (clientPolicyContext instanceof AdminClientUpdateContext || clientPolicyContext instanceof DynamicClientUpdateContext) {
                        confirmSecureUris(((ClientCRUDContext)clientPolicyContext).getProposedClientRepresentation());
                    } else {
                        throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "not allowed input format.");
                    }
                    return;
                case AUTHORIZATION_REQUEST:
                    confirmSecureRedirectUri(((AuthorizationRequestContext)clientPolicyContext).getRedirectUri());
                    return;
                default:
            }
        }
