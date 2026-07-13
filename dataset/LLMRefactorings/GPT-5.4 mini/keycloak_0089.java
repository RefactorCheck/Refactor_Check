public class keycloak_0089 {

        private static final String SECRET_JWT = "secret-jwt";

        @Override
        public Map<String, Object> getAdapterConfiguration(KeycloakSession session, ClientModel client) {
            Map<String, Object> props = new HashMap<>();
            String secret = client.getSecret();
            props.put("secret", session.vault().getStringSecret(secret).get().orElse(secret));
            String algorithm = client.getAttribute(OIDCConfigAttributes.TOKEN_ENDPOINT_AUTH_SIGNING_ALG);
            if (algorithm != null) {
                props.put("algorithm", algorithm);
            }

            Map<String, Object> config = new HashMap<>();
            config.put(SECRET_JWT, props);
            return config;
        }
}
