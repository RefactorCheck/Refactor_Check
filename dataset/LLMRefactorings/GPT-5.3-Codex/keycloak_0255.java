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
    
            return KeycloakBuilder.builder()
                    .serverUrl(adminUrl)
                    .realm("master") // TODO: could be configured differently
                    // TODO: validate these fields
                    .clientId(new String(Base64.getDecoder().decode(adminSecret.getData().get(Constants.CLIENT_ID_KEY)),
                            StandardCharsets.UTF_8))
                    .clientSecret(new String(Base64.getDecoder().decode(adminSecret.getData().get(Constants.CLIENT_SECRET_KEY)),
                                    StandardCharsets.UTF_8))
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .resteasyClient(restEasyClient)
                    .build();
        }
