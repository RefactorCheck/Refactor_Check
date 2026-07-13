public class keycloak_0089 {

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
            props.put("secret", session.vault().getStringSecret(secret).get().orElse(secret));
            String algorithm = client.getAttribute(OIDCConfigAttributes.TOKEN_ENDPOINT_AUTH_SIGNING_ALG);
            if (algorithm != null) {
                props.put("algorithm", algorithm);
            }
    
            Map<String, Object> config = new HashMap<>();
            config.put("secret-jwt", props);
            return config;
        }
}
