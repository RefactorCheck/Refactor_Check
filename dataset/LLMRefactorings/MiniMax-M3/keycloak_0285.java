public class keycloak_0285 {

        protected void addAccountConsoleClient(RealmModel realm) {
            if (realm.getClientByClientId(Constants.ACCOUNT_CONSOLE_CLIENT_ID) == null) {
                ClientModel client = KeycloakModelUtils.createPublicClient(realm, Constants.ACCOUNT_CONSOLE_CLIENT_ID);
                client.setName("${client_" + Constants.ACCOUNT_CONSOLE_CLIENT_ID + "}");
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
    
                client.addProtocolMapper(createAudienceResolveMapper());
            }
        }
    
        private ProtocolMapperModel createAudienceResolveMapper() {
            ProtocolMapperModel audienceMapper = new ProtocolMapperModel();
            audienceMapper.setName("audience resolve");
            audienceMapper.setProtocol("openid-connect");
            audienceMapper.setProtocolMapper("oidc-audience-resolve-mapper");
            return audienceMapper;
        }
}
