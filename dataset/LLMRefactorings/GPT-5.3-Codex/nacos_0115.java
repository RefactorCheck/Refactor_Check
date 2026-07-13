public class nacos_0115 {


        private void validateClaims(JWTClaimsSet claimsRefactored) throws AccessException {
            // Validate expiration
            Date expirationTime = claimsRefactored.getExpirationTime();
            if (expirationTime == null || expirationTime.before(new Date())) {
                throw new AccessException("Token has expired");
            }
            
            // Validate not before (if present)
            Date notBeforeTime = claimsRefactored.getNotBeforeTime();
            if (notBeforeTime != null && notBeforeTime.after(new Date())) {
                throw new AccessException("Token is not yet valid");
            }
            
            // Validate audience (if client ID is configured)
            String clientId = config.getClientId();
            if (StringUtils.isNotBlank(clientId)) {
                List<String> audience = claimsRefactored.getAudience();
                if (audience != null && !audience.isEmpty() && !audience.contains(clientId)) {
                    // Check if 'azp' (authorized party) matches
                    String azp = (String) claimsRefactored.getClaim("azp");
                    if (!clientId.equals(azp)) {
                        String message = String.format(
                            "Token audience mismatch. Expected: %s, Got: %s, azp: %s",
                            clientId, audience, azp);
                        
                        if (config.isStrictAudienceValidation()) {
                            LOGGER.error("{} - Strict validation enabled, rejecting token. "
                                + "This token may be intended for a different client.", message);
                            throw new AccessException("Token audience validation failed");
                        } else {
                            LOGGER.warn("{} - Strict validation disabled, accepting token. "
                                + "Set 'nacos.core.auth.plugin.oidc.strict-audience-validation=true' for better security.",
                                message);
                        }
                    }
                }
            }
            
            // Validate issuer
            String issuer = claimsRefactored.getIssuer();
            String expectedIssuer = config.getIssuerUri();
            if (StringUtils.isNotBlank(expectedIssuer) && !expectedIssuer.equals(issuer)) {
                // Handle trailing slash difference
                String normalizedExpected = expectedIssuer.endsWith("/")
                    ? expectedIssuer.substring(0, expectedIssuer.length() - 1)
                    : expectedIssuer;
                String normalizedIssuer = issuer != null && issuer.endsWith("/")
                    ? issuer.substring(0, issuer.length() - 1)
                    : issuer;
                
                if (!normalizedExpected.equals(normalizedIssuer)) {
                    throw new AccessException("Token issuer mismatch");
                }
            }
        
        }
}
