public class keycloak_0255 {

    public static org.keycloak.admin.client.Keycloak getAdminClient(KubernetesClient client, Keycloak keycloak, String addressOverride) {
        Secret adminSecret = client.resources(Secret.class)
                .inNamespace(keycloak.getMetadata().getNamespace())
                .withName(keycloak.getMetadata().getName() + "-admin").require();

        String adminUrl = getAdminUrl(keycloak, client, addressOverride);

        Client restEasyClient = null;

        // create a custom client if using https/mtls
        if (adminUrl.startsWith(HTTPS)) {
            restEasyClient = createRestEasyClient(client, keycloak, restEasyClient);
        }

        String clientId = decodeSecretValue(adminSecret, Constants.CLIENT_ID_KEY);
        String clientSecret = decodeSecretValue(adminSecret, Constants.CLIENT_SECRET_KEY);

        return KeycloakBuilder.builder()
                .serverUrl(adminUrl)
                .realm("master") // TODO: could be configured differently
                // TODO: validate these fields
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .resteasyClient(restEasyClient)
                .build();
    }

    private static String decodeSecretValue(Secret secret, String key) {
        return new String(Base64.getDecoder().decode(secret.getData().get(key)),
                StandardCharsets.UTF_8);
    }
}
