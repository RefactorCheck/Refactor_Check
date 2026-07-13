public class keycloak_0077 {

    public SimpleHttpRequest generateTokenRequest(String authorizationCode) {
        KeycloakContext context = session.getContext();
        OAuth2IdentityProviderConfig providerConfig = provider.getConfig();
        SimpleHttpRequest tokenRequest = SimpleHttp.create(session).doPost(providerConfig.getTokenUrl())
                .param(OAUTH2_PARAMETER_CODE, authorizationCode)
                .param(OAUTH2_PARAMETER_REDIRECT_URI, Urls.identityProviderAuthnResponse(context.getUri().getBaseUri(),
                        providerConfig.getAlias(), context.getRealm().getName()).toString())
                .param(OAUTH2_PARAMETER_GRANT_TYPE, OAUTH2_GRANT_TYPE_AUTHORIZATION_CODE);

        if (providerConfig.isPkceEnabled()) {
            String codeVerifier = lookupCodeVerifier(context);
            if (codeVerifier == null) {
                return tokenRequest;
            }
            tokenRequest.param(OAuth2Constants.CODE_VERIFIER, codeVerifier);
        }

        return provider.authenticateTokenRequest(tokenRequest);
    }

    private String lookupCodeVerifier(KeycloakContext context) {
        String stateParam = session.getContext().getUri().getQueryParameters().getFirst(OAuth2Constants.STATE);
        if (stateParam == null) {
            logger.warn("Cannot lookup PKCE code_verifier: state param is missing.");
            return null;
        }

        RealmModel realm = context.getRealm();
        IdentityBrokerState idpBrokerState = IdentityBrokerState.encoded(stateParam, realm);
        ClientModel client = realm.getClientByClientId(idpBrokerState.getClientId());

        AuthenticationSessionModel authSession = ClientSessionCode.getClientSession(
                idpBrokerState.getEncoded(),
                idpBrokerState.getTabId(),
                session,
                realm,
                client,
                event,
                AuthenticationSessionModel.class);

        if (authSession == null) {
            logger.warnf("Cannot lookup PKCE code_verifier: authSession not found. state=%s", stateParam);
            return null;
        }

        String brokerCodeChallenge = authSession.getClientNote(BROKER_CODE_CHALLENGE_PARAM);
        if (brokerCodeChallenge == null) {
            logger.warnf("Cannot lookup PKCE code_verifier: brokerCodeChallenge not found. state=%s", stateParam);
            return null;
        }

        return brokerCodeChallenge;
    }
}
