public class keycloak_0074 {

    public TokenVerifier<T> parse() throws VerificationException {
        if (jws == null) {
            if (tokenString == null) {
                throw new VerificationException("Token not set");
            }

            jws = parseJws();
            token = readToken();
        }
        return this;
    }

    private JWSInput parseJws() throws VerificationException {
        try {
            return new JWSInput(tokenString);
        } catch (JWSInputException e) {
            throw new VerificationException("Failed to parse JWT", e);
        }
    }

    private T readToken() throws VerificationException {
        try {
            return jws.readJsonContent(clazz);
        } catch (JWSInputException e) {
            throw new VerificationException("Failed to read access token from JWT", e);
        }
    }
}
