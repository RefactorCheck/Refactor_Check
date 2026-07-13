public class nacos_0186 {

        private OIDCTokens exchangeCodeForTokens(String code, String redirectUri) throws Exception {
            String tokenEndpoint = config.getTokenEndpoint();
            if (StringUtils.isBlank(tokenEndpoint)) {
                throw new AccessException("Token endpoint not configured");
            }

            TokenRequest tokenRequest = buildTokenRequest(code, redirectUri, tokenEndpoint);

            TokenResponse tokenResponse =
                OIDCTokenResponseParser.parse(tokenRequest.toHTTPRequest().send());

            handleTokenResponseErrors(tokenResponse);

            OIDCTokenResponse oidcResponse = (OIDCTokenResponse) tokenResponse.toSuccessResponse();
            return oidcResponse.getOIDCTokens();
        }

        private TokenRequest buildTokenRequest(String code, String redirectUri, String tokenEndpoint) {
            AuthorizationCode authCode = new AuthorizationCode(code);
            AuthorizationGrant grant = new AuthorizationCodeGrant(authCode, URI.create(redirectUri));

            ClientAuthentication clientAuth = new ClientSecretBasic(
                new ClientID(config.getClientId()),
                new Secret(config.getClientSecret()));

            return new TokenRequest(
                URI.create(tokenEndpoint),
                clientAuth,
                grant);
        }

        private void handleTokenResponseErrors(TokenResponse tokenResponse) {
            if (!tokenResponse.indicatesSuccess()) {
                String error = tokenResponse.toErrorResponse().getErrorObject().getDescription();
                LOGGER.error("Token exchange failed: {}", error);
                throw new AccessException("Token exchange failed: " + error);
            }
        }
}
