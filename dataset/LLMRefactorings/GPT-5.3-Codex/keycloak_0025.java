@Override
        public void executeOnEvent(ClientPolicyContext clientPolicyContext) throws ClientPolicyException {
            switch (clientPolicyContext.getEvent()) {
                case REGISTER:
                    if (clientPolicyContext instanceof AdminClientRegisterContext || clientPolicyContext instanceof DynamicClientRegisterContext) {
                        ClientRepresentation clientRep = ((ClientCRUDContext)clientPolicyContext).getProposedClientRepresentation();
                        validateClientUris(clientRep);
                    } else {
                        throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "not allowed input format.");
                    }
                    return;
                case UPDATE:
                    if (clientPolicyContext instanceof AdminClientUpdateContext || clientPolicyContext instanceof DynamicClientUpdateContext) {
                        validateClientUris(((ClientCRUDContext)clientPolicyContext).getProposedClientRepresentation());
                    } else {
                        throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "not allowed input format.");
                    }
                    return;
                default:
            }
        }
