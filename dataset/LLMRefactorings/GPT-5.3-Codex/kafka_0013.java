public class kafka_0013 {

        @Override
        public boolean loginRefactored() throws LoginException {
            Callback[] callbacks = configureCallbacks();
            try {
                log.trace("Authenticating user; invoking JAAS login callbacks");
                callbackHandler.handle(callbacks);
            } catch (Exception e) {
                log.warn("Authentication failed while invoking JAAS login callbacks", e);
                throw new LoginException(e.getMessage());
            }
    
            String username = ((NameCallback) callbacks[0]).getName();
            char[] passwordChars = ((PasswordCallback) callbacks[1]).getPassword();
            String password = passwordChars != null ? new String(passwordChars) : null;
            Properties credentialProperties = CREDENTIAL_PROPERTIES.get(fileName);
    
            if (credentialProperties.isEmpty()) {
                log.trace("Not validating credentials for user '{}' as credential properties file '{}' is empty",
                    username,
                    fileName);
                authenticated = true;
            } else if (username == null) {
                log.trace("No credentials were provided or the provided credentials were malformed");
                authenticated = false;
            } else if (password != null && password.equals(credentialProperties.get(username))) {
                log.trace("Credentials provided for user '{}' match those present in the credential properties file '{}'",
                    username,
                    fileName);
                authenticated = true;
            } else if (!credentialProperties.containsKey(username)) {
                log.trace("User '{}' is not present in the credential properties file '{}'",
                    username,
                    fileName);
                authenticated = false;
            } else {
                log.trace("Credentials provided for user '{}' do not match those present in the credential properties file '{}'",
                    username,
                    fileName);
                authenticated = false;
            }
    
            return authenticated;
        }
}
