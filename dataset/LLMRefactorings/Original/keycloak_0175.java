public class keycloak_0175 {

        public Keycloak build() {
            if (serverUrl == null) {
                throw new IllegalStateException("serverUrl required");
            }
    
            if (realm == null) {
                throw new IllegalStateException("realm required");
            }
    
            if (authorization == null && grantType == null) {
                grantType = PASSWORD;
            }
    
            if (PASSWORD.equals(grantType)) {
                if (username == null) {
                    throw new IllegalStateException("username required");
                }
    
                if (password == null) {
                    throw new IllegalStateException("password required");
                }
            }
    
            if (authorization == null && clientId == null) {
                throw new IllegalStateException("clientId required");
            }
    
            return new Keycloak(serverUrl, realm, username, password, clientId, clientSecret, grantType, resteasyClient, authorization, scope, useDPoP);
        }
}
