public class keycloak_0089 {

    private static final String SECRET_KEY = "secret";
    private static final String ALGORITHM_KEY = "algorithm";
    private static final String SECRET_JWT_KEY = "secret-jwt";

        @Override
        public Map<String, Object> getAdapterConfiguration(KeycloakSession session, ClientModel client) {
            // e.g. client adapter's keycloak.json
            // "credentials": {
            //   "secret-jwt": {
            //     "secret": "234234-234234-234234",
            //     "algorithm": "HS256"
            //   }
            // }
            Map<String, Object> props = new HashMap<>();
            String secret = client.getSecret();
            props.put(SECRET_KEY, session.vault().getStringSecret(secret).get().orElse(secret));
            String algorithm = client.getAttribute(OIDCConfigAttributes.TOKEN_ENDPOINT_AUTH_SIGNING_ALG);
            if (algorithm != null) {
                props.put(ALGORITHM_KEY, algorithm);
            }
    
            Map<String, Object> config = new HashMap<>();
            config.put(SECRET_JWT_KEY, props);
            return config;
        }
}
