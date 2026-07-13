public class keycloak_0100 {

        @Override
        public Response process(Context context) {
            setContext(context);
    
            event.detail(Details.AUTH_METHOD, "token_exchange");
            event.client(client);
    
            TokenExchangeContext exchange = new TokenExchangeContext(
                    session,
                    formParams,
                    cors,
                    realm,
                    event,
                    client,
                    clientConnection,
                    headers,
                    tokenManager,
                    clientAuthAttributes);
    
            var providerFactories = session.getKeycloakSessionFactory()
                    .getProviderFactoriesStream(TokenExchangeProvider.class);
            TokenExchangeProvider tokenExchangeProvider = providerFactories
                    .sorted((f1, f2) -> f2.order() - f1.order())
                    .map(f -> session.getProvider(TokenExchangeProvider.class, f.getId()))
                    .filter(p -> p.supports(exchange))
                    .findFirst()
                    .orElseThrow(() -> {
                        if (exchange.getUnsupportedReason() != null) {
                            event.detail(Details.REASON, exchange.getUnsupportedReason());
                            event.error(Errors.INVALID_REQUEST);
                            return new CorsErrorResponseException(cors, OAuthErrorException.INVALID_REQUEST, exchange.getUnsupportedReason(), Response.Status.BAD_REQUEST);
                        } else {
                            return new InternalServerErrorException("No token exchange provider available");
                        }
                    });
    
            try {
                //trigger if there is a supported token exchange provider
                session.clientPolicy().triggerOnEvent(new TokenExchangeRequestContext(exchange));
            } catch (ClientPolicyException cpe) {
                event.detail(Details.REASON, Details.CLIENT_POLICY_ERROR);
                event.detail(Details.CLIENT_POLICY_ERROR, cpe.getError());
                event.detail(Details.CLIENT_POLICY_ERROR_DETAIL, cpe.getErrorDetail());
                event.error(cpe.getError());
                throw new CorsErrorResponseException(cors, cpe.getError(), cpe.getErrorDetail(), cpe.getErrorStatus());
            }
    
            return tokenExchangeProvider.exchange(exchange);
        }
}
