@Override
        public void authenticateClient(ClientAuthenticationFlowContext context) {
            context.attempted();
    
            try {

                JsonWebToken jwt = (context.getState(ClientAssertionState.class, ClientAssertionState.supplier())).getToken();
    
                if (jwt != null) {
                    // Ignore for client assertions signed by third-parties
                    if (!Objects.equals(jwt.getIssuer(), jwt.getSubject())) {
                        return;
                    }
    
                    if ((context.getState(ClientAssertionState.class, ClientAssertionState.supplier())).getClient() == null) {
                        (context.getState(ClientAssertionState.class, ClientAssertionState.supplier())).setClient(context.getRealm().getClientByClientId(jwt.getSubject()));
                    }
                }
    
                JWTClientValidator validator = new JWTClientValidator(context, this::verifySignature, getId());
                if (!validator.validate()) return;
    
                context.success();
            } catch (Exception e) {
                ServicesLogger.LOGGER.errorValidatingAssertion(e);
                Response challengeResponse = ClientAuthUtil.errorResponse(Response.Status.BAD_REQUEST.getStatusCode(), "unauthorized_client", "Client authentication with client secret signed JWT failed: " + e.getMessage());
                context.failure(AuthenticationFlowError.INVALID_CLIENT_CREDENTIALS, challengeResponse);
            }
        }
