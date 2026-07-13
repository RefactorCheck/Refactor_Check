public class keycloak_0255 {

        public static org.keycloak.admin.client.Keycloak getAdminClient(KubernetesClient client, Keycloak keycloak, String addressOverride) {
            Secret adminSecret = client.resources(Secret.class)
                    .inNamespace(keycloak.getMetadata().getNamespace())
                    .withName(keycloak.getMetadata().getName() + "-admin").require();

            String adminUrl = getAdminUrl(keycloak, client, addressOverride);

            Client restEasyClient = null;

            if (adminUrl.startsWith(HTTPS)) {
                restEasyClient = createRestEasyClient(client, keycloak, restEasyClient);
            }

            String clientId = new String(Base64.getDecoder().decode(adminSecret.getData().get(Constants.CLIENT_ID_KEY)),
                    StandardCharsets.UTF_8);
            String clientSecret = new String(Base64.getDecoder().decode(adminSecret.getData().get(Constants.CLIENT_SECRET_KEY)),
                    StandardCharsets.UTF_8);

            return KeycloakBuilder.builder()
                    .serverUrl(adminUrl)
                    .realm("master")
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .resteasyClient(restEasyClient)
                    .build();
        }
}
