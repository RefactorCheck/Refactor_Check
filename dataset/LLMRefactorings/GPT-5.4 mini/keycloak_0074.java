public class keycloak_0074 {

        private static final String TOKEN_NOT_SET = "Token not set";
        private static final String FAILED_TO_PARSE_JWT = "Failed to parse JWT";
        private static final String FAILED_TO_READ_ACCESS_TOKEN_FROM_JWT = "Failed to read access token from JWT";

        public TokenVerifier<T> parse() throws VerificationException {
            if (jws == null) {
                if (tokenString == null) {
                    throw new VerificationException(TOKEN_NOT_SET);
                }
    
                try {
                    jws = new JWSInput(tokenString);
                } catch (JWSInputException e) {
                    throw new VerificationException(FAILED_TO_PARSE_JWT, e);
                }
    
    
                try {
                    token = jws.readJsonContent(clazz);
                } catch (JWSInputException e) {
                    throw new VerificationException(FAILED_TO_READ_ACCESS_TOKEN_FROM_JWT, e);
                }
            }
            return this;
        }
}
