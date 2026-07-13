public class keycloak_0175 {

    public Keycloak build() {
        validateServerUrl();
        validateRealm();

        if (authorization == null && grantType == null) {
            grantType = PASSWORD;
        }

        if (PASSWORD.equals(grantType)) {
            validatePasswordGrant();
        }

        validateClientId();

        return new Keycloak(serverUrl, realm, username, password, clientId, clientSecret, grantType, resteasyClient, authorization, scope, useDPoP);
    }

    private void validateServerUrl() {
        if (serverUrl == null) {
            throw new IllegalStateException("serverUrl required");
        }
    }

    private void validateRealm() {
        if (realm == null) {
            throw new IllegalStateException("realm required");
        }
    }

    private void validatePasswordGrant() {
        if (username == null) {
            throw new IllegalStateException("username required");
        }
        if (password == null) {
            throw new IllegalStateException("password required");
        }
    }

    private void validateClientId() {
        if (authorization == null && clientId == null) {
            throw new IllegalStateException("clientId required");
        }
    }
}
