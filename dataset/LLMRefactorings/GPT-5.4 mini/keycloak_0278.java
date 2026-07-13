public class keycloak_0278 {

        @Override
        public void authenticateClient(ClientAuthenticationFlowContext context) {
            context.attempted();

            try {
                ClientAssertionState clientAssertionState = context.getState(ClientAssertionState.class, ClientAssertionState.supplier());
                JsonWebToken jwt = clientAssertionState.getToken();

                if (jwt != null) {
                    // Ignore for client assertions signed by third-parties
                    boolean thirdPartyAssertion = !Objects.equals(jwt.getIssuer(), jwt.getSubject());
                    if (thirdPartyAssertion) {
                        return;
                    }

                    if (clientAssertionState.getClient() == null) {
                        clientAssertionState.setClient(context.getRealm().getClientByClientId(jwt.getSubject()));
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
}
