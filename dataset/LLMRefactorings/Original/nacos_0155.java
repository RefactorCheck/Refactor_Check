public class nacos_0155 {

        public OidcUser exchangeCodeForUser(String code, String state, String redirectUri)
            throws AccessException {
            try {
                // Verify and decode state (self-contained, no cache lookup needed)
                StateData stateData = verifyAndDecodeState(state);
                if (stateData == null) {
                    throw new AccessException("Invalid or expired state parameter");
                }
                
                // Exchange code for tokens
                OIDCTokens tokens = exchangeCodeForTokens(code, redirectUri);
                
                // Validate ID token
                String idTokenString = tokens.getIDTokenString();
                JWTClaimsSet claims = tokenValidator.validate(idTokenString);
                
                // Verify nonce matches (protects against token replay attacks)
                String tokenNonce = (String) claims.getClaim("nonce");
                
                if (tokenNonce == null) {
                    String message = "Nonce not present in ID token";
                    if (config.isStrictNonceValidation()) {
                        LOGGER.error("{} - Strict validation enabled, rejecting authentication",
                            message);
                        throw new AccessException(message
                            + ". Set 'nacos.core.auth.plugin.oidc.strict-nonce-validation=false' "
                            + "if your IdP doesn't support nonce.");
                    } else {
                        LOGGER.warn("{} - Strict validation disabled, allowing authentication. "
                            + "This reduces protection against replay attacks.", message);
                    }
                } else if (!stateData.nonce.equals(tokenNonce)) {
                    String message = String.format("Nonce mismatch: expected %s, got %s",
                        stateData.nonce, tokenNonce);
                    LOGGER.error("{} - Possible token replay attack detected", message);
                    throw new AccessException(message);
                }
                
                // Map claims to user
                OidcUser user = userMapper.mapToUser(claims);
                user.setToken(tokens.getAccessToken().getValue());
                
                LOGGER.info("User authenticated via authorization code: {}", user.getUsername());
                return user;
                
            } catch (AccessException e) {
                throw e;
            } catch (Exception e) {
                LOGGER.error("Failed to exchange code for tokens", e);
                throw new AccessException("Authentication failed: " + e.getMessage());
            }
        }
}
