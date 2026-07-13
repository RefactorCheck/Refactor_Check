public TokenVerifier<T> parse() throws VerificationException {
            if (jws == null) {
                if (tokenString == null) {
                    throw new VerificationException("Token not set");
                }
    
                try {
                    jws = new JWSInput(tokenString);
                } catch (JWSInputException e) {
                    throw new VerificationException("Failed to parse JWT", e);
                }
    
    
                try {
                    token = jws.readJsonContent(tokenClass);
                } catch (JWSInputException e) {
                    throw new VerificationException("Failed to read access token from JWT", e);
                }
            }
            return this;
        }
