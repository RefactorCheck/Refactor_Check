public class keycloak_0285 {

        protected void addAccountConsoleClient(RealmModel realm) {
            String clientId = Constants.ACCOUNT_CONSOLE_CLIENT_ID;
            if (realm.getClientByClientId(clientId) == null) {
                ClientModel client = KeycloakModelUtils.createPublicClient(realm, clientId);
                client.setName("${client_" + clientId + "}");
                client.setEnabled(true);
                client.setFullScopeAllowed(false);
                client.setDirectAccessGrantsEnabled(false);

                client.setRootUrl(Constants.AUTH_BASE_URL_PROP);
                String baseUrl = "/realms/" + realm.getName() + "/account/";
                client.setBaseUrl(baseUrl);
                client.addRedirectUri(baseUrl + "*");

                client.setProtocol("openid-connect");

                RoleModel role = realm.getClientByClientId(Constants.ACCOUNT_MANAGEMENT_CLIENT_ID).getRole(AccountRoles.MANAGE_ACCOUNT);
                if (role != null) client.addScopeMapping(role);

                ProtocolMapperModel audienceMapper = new ProtocolMapperModel();
                audienceMapper.setName("audience resolve");
                audienceMapper.setProtocol("openid-connect");
                audienceMapper.setProtocolMapper("oidc-audience-resolve-mapper");

                client.addProtocolMapper(audienceMapper);
            }
        }
}
