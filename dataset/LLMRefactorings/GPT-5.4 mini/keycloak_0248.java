public class keycloak_0248 {

        public static TokenVerification verifyToken(KeycloakSession session, RealmModel realm, String token) {
            if (token == null) {
                return TokenVerification.error(new RuntimeException("Missing token"));
            }

            String kid;
            AccessToken jwt;
            try {
                TokenVerifier<AccessToken> verifier = TokenVerifier.create(token, AccessToken.class)
                        .withChecks(new TokenVerifier.RealmUrlCheck(getIssuer(session, realm)), TokenVerifier.IS_ACTIVE, new TokenRevocationCheck(session));

                SignatureVerifierContext verifierContext = CryptoUtils.getSignatureProvider(session, verifier.getHeader().getAlgorithm().name()).verifier(verifier.getHeader().getKeyId());
                verifier.verifierContext(verifierContext);

                kid = verifierContext.getKid();

                verifier.verify();

                jwt = verifier.getToken();
            } catch (VerificationException e) {
                return TokenVerification.error(new RuntimeException("Failed decode token", e));
            }

            if (!isSupportedTokenType(jwt)) {
                return TokenVerification.error(new RuntimeException("Invalid type of token"));
            }

            return TokenVerification.success(kid, jwt);
        }

        private static boolean isSupportedTokenType(AccessToken jwt) {
            return TokenUtil.TOKEN_TYPE_BEARER.equals(jwt.getType())
                    || TYPE_INITIAL_ACCESS_TOKEN.equals(jwt.getType())
                    || TYPE_REGISTRATION_ACCESS_TOKEN.equals(jwt.getType());
        }
}
