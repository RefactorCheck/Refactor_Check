public class nacos_0186 {

        private OIDCTokens exchangeCodeForTokensRefactored(String code, String redirectUri) throws Exception {
            String tokenEndpoint = config.getTokenEndpoint();
            if (StringUtils.isBlank(tokenEndpoint)) {
                throw new AccessException("Token endpoint not configured");
            }
            
            // Build token request
            AuthorizationCode authCode = new AuthorizationCode(code);
            AuthorizationGrant grant = new AuthorizationCodeGrant(authCode, URI.create(redirectUri));
            
            // Client authentication
            ClientAuthentication clientAuth = new ClientSecretBasic(
                new ClientID(config.getClientId()),
                new Secret(config.getClientSecret()));
            
            // Send token request
            TokenRequest tokenRequest = new TokenRequest(
                URI.create(tokenEndpoint),
                clientAuth,
                grant);
            
            TokenResponse tokenResponse =
                OIDCTokenResponseParser.parse(tokenRequest.toHTTPRequest().send());
            
            if (!tokenResponse.indicatesSuccess()) {
                String error = tokenResponse.toErrorResponse().getErrorObject().getDescription();
                LOGGER.error("Token exchange failed: {}", error);
                throw new AccessException("Token exchange failed: " + error);
            }
            
            OIDCTokenResponse oidcResponse = (OIDCTokenResponse) tokenResponse.toSuccessResponse();
            return oidcResponse.getOIDCTokens();
        }
}
